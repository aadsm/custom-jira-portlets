/*
 * @author António Afonso
 */
package net.aadsm.jira.plugin.portlets.custom;

import com.atlassian.configurable.ObjectConfigurationException;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.ManagerFactory;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkType;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.plugin.elements.ResourceDescriptor;

import org.apache.log4j.Logger;
import org.apache.velocity.app.Velocity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchRequestPortlet extends com.atlassian.jira.portal.portlets.SearchRequestPortlet
{

    // TODO: I can probably detect this on runtime
    private static final int NUMBER_COLUMNS = 10;
    private static final String FIELD_NONE = "none";
    private final Logger logger = Logger.getLogger(SearchRequestPortlet.class);
    private final IssueLinkManager linkManager;
    private final IssueLinkTypeManager issueLinkTypeManager;
    private FieldManager fieldManager;
    
    public SearchRequestPortlet(JiraAuthenticationContext authenticationContext, 
                                PermissionManager permissionManager,
                                ConstantsManager constantsManager,
                                SearchProvider searchProvider,
                                ApplicationProperties applicationProperties,
                                SearchRequestService searchRequestService)
    {
        super( authenticationContext, permissionManager, constantsManager,
               searchProvider, applicationProperties, searchRequestService );
        this.linkManager = ComponentManager.getInstance().getIssueLinkManager();
        this.issueLinkTypeManager = (IssueLinkTypeManager) ComponentManager.getInstance().getContainer().getComponentInstanceOfType(IssueLinkTypeManager.class);
        this.fieldManager = ManagerFactory.getFieldManager();
    }
    
    protected Map getVelocityParams(PortletConfiguration portletConfiguration)
    {
        Map params = super.getVelocityParams(portletConfiguration);
        List columns = new Vector();
        Map headers = new HashMap();
                
        try
        {
            params.put("showheader", portletConfiguration.getProperty("showheader"));
            
            for( int i = 1; i <= NUMBER_COLUMNS; i++ )
            {
                String value = portletConfiguration.getProperty("column"+i);
                
                if( ! FIELD_NONE.equals(value) )
                {
                    columns.add(value);
                    headers.put( value, getHeader(value) );
                }
            }
        }
        catch( ObjectConfigurationException ex )
        {
        	logger.error("An exception occurred while getting Velocity params. ", ex);
        }
        
        params.put("columns", columns);
        params.put("headers", headers);
        params.put("tmpls", getColumnTemplates());
        params.put("issueLinkManager", linkManager);
        
        return params;
    }
    
    protected Map getColumnTemplates()
    {
        Map tmpls = new HashMap();
        
        List rsrcs = getDescriptor().getResourceDescriptors("velocity");
        Iterator it = rsrcs.iterator();
        
        while( it.hasNext() )
        {
            ResourceDescriptor rsrc = (ResourceDescriptor)it.next();
            tmpls.put(rsrc.getName(), rsrc.getLocation());
        }
        
        return tmpls;
    }
    
    public String getHeader(String column)
    {
    	String[] params;
        if( column.startsWith("issuelink_", 0) && (params = column.split("_")).length == 3 )
        {
            IssueLinkType issueLinkType = issueLinkTypeManager.getIssueLinkType(Long.valueOf(params[1]) );
            if( "inward".equals( params[2]) )
            {
                return issueLinkType.getInward();
            }
            else
            {
                return issueLinkType.getOutward();
            }
        }
        else
        {
            return fieldManager.getField(column).getName();
        }
    }
}
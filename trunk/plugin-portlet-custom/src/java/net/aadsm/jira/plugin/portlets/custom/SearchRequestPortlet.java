/*
 * @author António Afonso
 */
package net.aadsm.jira.plugin.portlets.custom;

import com.atlassian.configurable.ObjectConfigurationException;
import com.atlassian.jira.ManagerFactory;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.portal.PortletConfiguration;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import org.apache.velocity.app.Velocity;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class SearchRequestPortlet extends com.atlassian.jira.portal.portlets.SearchRequestPortlet
{
    // TODO: I can probably detect this on runtime
    private static final int NUMBER_COLUMNS = 10;
    private static final String CONFIG_PREFIX = "portlet.custom.searchrequest.";
    private static final String PROPERTY_COLUMN_PREFIX = CONFIG_PREFIX + "field.column.";
    private static final String FIELD_NONE = "none";
    
    public SearchRequestPortlet(JiraAuthenticationContext authenticationContext, 
                                PermissionManager permissionManager,
                                ConstantsManager constantsManager,
                                SearchProvider searchProvider,
                                ApplicationProperties applicationProperties,
                                SearchRequestService searchRequestService)
    {
        super( authenticationContext, permissionManager, constantsManager,
               searchProvider, applicationProperties, searchRequestService );
    }
    
    protected Map getVelocityParams(PortletConfiguration portletConfiguration)
    {
        Map params = super.getVelocityParams(portletConfiguration);
        List columns = new Vector();
                
        try
        {
            params.put("showheader", portletConfiguration.getProperty("showheader"));
            
            for( int i = 1; i <= NUMBER_COLUMNS; i++ )
            {
                String value = portletConfiguration.getProperty("column"+i);
                
                if( ! FIELD_NONE.equals(value) )
                {
                    columns.add(value);
                }
            }
        }
        catch( ObjectConfigurationException ex )
        {
            ex.printStackTrace();
        }
        
        params.put("columns", columns);
        params.put("fm", ManagerFactory.getFieldManager());
        
        return params;
    }
}
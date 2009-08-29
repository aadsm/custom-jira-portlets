/*
 * @author António Afonso
 */
package net.aadsm.jira.portal;

import com.atlassian.configurable.ValuesGenerator;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.ManagerFactory;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkType;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.bean.I18nBean;
import com.opensymphony.user.User;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FieldValuesGenerator implements ValuesGenerator
{
    public Map getValues(Map params)
    {
        Map values = new LinkedHashMap();
        
        values.put("none", "-- None --");
        
        try
        {
            Set fields = ManagerFactory.getFieldManager().getAvailableNavigableFields(getLoggedInUser());
            Iterator it = fields.iterator();
            Field field;
            
            while( it.hasNext() )
            {
                field = (Field)it.next();
                values.put(field.getId(), field.getName());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        if( values.containsKey("issuelinks") )
        {
            String desc = (String)values.remove("issuelinks");
            IssueLinkTypeManager issueLinkTypeManager = (IssueLinkTypeManager)ComponentManager.getComponentInstanceOfType(IssueLinkTypeManager.class);
            Iterator it = issueLinkTypeManager.getIssueLinkTypes().iterator();
            
            while( it.hasNext() )
            {
                IssueLinkType type = (IssueLinkType)it.next();
                values.put( "issuelink_" + type.getId() + "_inward", desc + " - " + type.getInward() );
                values.put( "issuelink_" + type.getId() + "_outward", desc + " - " + type.getOutward() );
            }
        }
        
        return values;
    }
    
    protected JiraAuthenticationContext getJiraAuthenticationContext()
    {
        return ComponentManager.getInstance().getJiraAuthenticationContext();
    }
    
    protected I18nBean getI18nBean()
    {
        return getJiraAuthenticationContext().getI18nBean();
    }
    
    protected User getLoggedInUser()
    {
        return getJiraAuthenticationContext().getUser();
    }
}

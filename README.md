= Introduction =
Currently this project only consists of one portlet, it shows a selected saved filter/search giving the user the hability to select which fields to be displayed.
All fields are supported: the basic ones, custom and link fields.

= How to compile =

Enter into the project's directory and type:
{{{
maven jar
}}}
The jar will be under `target/afonso-jira-<project>-x.y.jar`

= How to install =
Copy the jar file into the `lib` directory of your JIRA® installation
{{{
cp afonso-jira-<project>-x.y.jar <jira>/atlassian-jira/WEB-INF/lib/
}}}
Restart JIRA
{{{
<jira>/bin/shutdown.sh && <jira>/bin/startup.sh
}}}

= `plugin-portlet-custom` =
== How to customize ==
You can modify or add your own templates that defines how each issue field will be render.
All field templates are located in
{{{
plugin-portlet-custom/src/etc/templates/custom/columns
}}}
Each `issuesummary-<field>.vm` file represents a field, you can edit these ones to modify the current render or create your own for fields that are using the default rendering.
If you want to create your own render you'll need to add an entry to `plugin-portlet-custom/src/etc/atlassian-plugin.xml` under 
{{{
<portlet key="customsearchrequest">
}}}
to register the new template, here's an example:
{{{
<resource type="velocity" name="<field>" location="templates/custom/columns/issuesummary-<field>.vm" />
}}}
The name MUST be equal to the internal name used by JIRA® to represent that field.

= Screenshots =
  * *Configuration Screen*
  [codegoogle/Configuration.png]
  * *Select Field*
  [codegoogle/Select%20Field.png]
  * *Portlet*
  [codegoogle/Portlet%20Without%20Header.png]
  * *Portlet With Headings*
  [codegoogle/Portlet%20With%20Header.png]

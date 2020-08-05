<#include "../include/imports.ftl">

<@hst.setBundle basename="essentials.homepage"/>

<div>
  <@hst.include ref="container"/>
</div>
<div>
  <@properties.property name="some.label" documentPath="properties" managerPostfix="properties"/></b></p>
  <@properties.property name="section.oneliner" documentPath="properties" var="oneliner" managerPostfix="properties"/>
  ${oneliner}
</div>
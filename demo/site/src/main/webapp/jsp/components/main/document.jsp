<%--
    Copyright 2010 Hippo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix='hst' uri="http://www.hippoecm.org/jsp/hst/core"%>
<%@ taglib prefix='properties' uri="http://www.onehippo.org/properties/jsp/tags"%>

<div>


  <p style="color:red"><b>${labels['header.text']}</b></p>
  <table border="1" cellpadding="5" cellspacing="0">
    <tr>
      <td><span style="color:red">${labels['field.title']}</span></td>
      <td>${document.title}</td>
    </tr>
    <tr>
      <td><span style="color:red">${labels['field.summary']}</span></td>
      <td>${document.summary}</td>
    </tr>
    <tr>
      <td><span style="color:red">${labels['field.body']}</span></td>
      <td><hst:html hippohtml="${document.body}"/></td>
    </tr>
  </table>
  <hr/>
  <p style="color:red"><b><properties:property name="section.title" documentPath="home/home"/></b></p>
  <properties:property name="section.oneliner" documentPath="home/home" var="oneliner"/>
  <p style="color:red">${oneliner}</p>
  <hr/>
  <div style="color:gray">This is a faulty property reference: <properties:property name="section.oneliner.nonexisting" documentPath="home/home" /></div>
  <hr/>
  <p><b>Something new again! (version 2.07.01)</b></p>
  <p>This is a property retrieved from outside site content root under Administration. <br/>
    It also uses latest PropertiesManager API with a Locale so language variants are found by linked documents.</p>
  <table border="1" cellpadding="5" cellspacing="0">
    <tr>
      <td>admin.site.title</td>
      <td><span style="color:red">${adminProperties['admin.site.title']}</span></td>
    </tr>
  </table>
  <hr/>
  <p><b>New in version 2.07.04: </b></p>
  <p>The 'language' attribute of the &lt;properties:property /&gt JSP tag. It fixes the language so it's no longer request based.<br/>
    Here it is set to 'en' so this label won't change to Dutch: <span style="color:red"><properties:property name="section.title" documentPath="home/home" language="en"/></span></p>
  <hr/>
  <p>* texts in red are CMS managed labels</p>
</div>
<hr/>
<div>
    <p><b>New in version 2.07.08: </b></p>
    <p>
        <span style="color:red">
    <properties:property name="date.long" documentPath="home/home"></b>
            <properties:param value="<%= new java.util.Date() %>"/>
    </properties:property>
        </span>
       <br/>
        You can also write is as
        <span style="color:red">
        <properties:property name="date.short" documentPath="home/home"></b>
            <properties:param value="<%= new java.util.Date() %>"/>
        </properties:property>
         </span> using new &lt;properties:param/&gt tag, like <br/>
            <br/>&lt;properties:property name="date.short" documentPath="home/home" &gt
                <br/>&lt;properties:param value="&lt;%= new java.util.Date() %&gt;"/&gt
            <br/>&lt;properties:property&gt

        <br/><span style="color:gray;font-size:12px">*In property file for key date.short value is <i>Today's date is {0,date,short}</i></span>
        <br/>
        <br/>
        By using &lt;properties:property /&gt you can format any string as you can do using  MessageFormat class<br/>

        <br/>&lt;properties:property name="test.string" documentPath="home/home" &gt
        <br/>&nbsp;&nbsp;&lt;properties:param name="Tom"/&gt
        <br/>&nbsp;&nbsp;&lt;properties:param value="Mac"/&gt
        <br/>&lt;/properties:property&gt
        <br/><span style="color:gray;font-size:12px">*In property file for key test.string value is <i>{0} and {1} are good friends</i></span>

        <br/><br/> Will result in:
        <span style="color:red">
            <properties:property name="test.string" documentPath="home/home"></b>
            <properties:param value="Tom"/>
            <properties:param value="Mac"/>
        </properties:property>
        </span>

        <br/>
    </p>
</div>
<hr/>

<content:title>
    <fmt:message key="edit.audio" />
</content:title>

<content:section cssId="audioEditPage">
    <h4><content:gettitle /></h4>
    <div class="card-panel">
        <form:form modelAttribute="audio" enctype="multipart/form-data">
            <tag:formErrors modelAttribute="audio" />

            <div class="row">
                <form:hidden path="locale" value="${audio.locale}" />
                <form:hidden path="revisionNumber" value="${audio.revisionNumber}" />
                <form:hidden path="audioFormat" value="${number.audioFormat}" />
                <form:hidden path="contentType" value="${number.contentType}" />
                
                <div class="input-field col s12">
                    <form:label path="transcription" cssErrorClass="error"><fmt:message key='transcription' /></form:label>
                    <form:input path="transcription" cssErrorClass="error" />
                </div>
                <div class="input-field col s12">
                    <select id="contentLicense" name="contentLicense">
                        <option value="">-- <fmt:message key='select' /> --</option>
                        <c:forEach var="contentLicense" items="${contentLicenses}">
                            <option value="${contentLicense.id}" <c:if test="${contentLicense == audio.contentLicense}">selected="selected"</c:if>><c:out value="${contentLicense}" /></option>
                        </c:forEach>
                    </select>
                    <label for="contentLicense"><fmt:message key="content.license" /></label>
                </div>
                <div class="input-field col s12">
                    <i class="material-icons prefix">link</i>
                    <form:label path="attributionUrl" cssErrorClass="error"><fmt:message key='attribution.url' /></form:label>
                    <form:input path="attributionUrl" cssErrorClass="error" type="url" />
                </div>
                <div class="file-field input-field col s12">
                    <div class="btn">
                        <span><fmt:message key='file' /></span>
                        <form:input path="bytes" type="file" />
                    </div>
                    <div class="file-path-wrapper">
                        <input class="file-path validate" type="text" />
                    </div>
                </div>
            </div>

            <button id="submitButton" class="btn waves-effect waves-light" type="submit">
                <fmt:message key="edit" /> <i class="material-icons right">send</i>
            </button>
            <a href="<spring:url value='/content/multimedia/audio/delete/${audio.id}' />" class="waves-effect waves-red red-text btn-flat right"><fmt:message key="delete" /></a>
        </form:form>
    </div>
    
    <div class="divider"></div>
    
    <h5><fmt:message key="revisions" /></h5>
    <table class="bordered highlight">
        <thead>
            <th><fmt:message key="revision" /></th>
            <th><fmt:message key="time" /></th>
            <th><fmt:message key="contributor" /></th>
        </thead>
        <tbody>
            <c:forEach var="contentCreationEvent" items="${contentCreationEvents}" varStatus="status">
                <tr>
                    <td>${fn:length(contentCreationEvents) - status.index}</td>
                    <td><fmt:formatDate value="${contentCreationEvent.calendar.time}" type="both" timeStyle="short" /></td>
                    <td>
                        <a href="<spring:url value='/content/community/contributors' />" target="_blank">
                            <div class="chip">
                                <img src="<spring:url value='${contentCreationEvent.contributor.imageUrl}' />" alt="${contentCreationEvent.contributor.firstName}" /> 
                                <c:out value="${contentCreationEvent.contributor.firstName}" />&nbsp;<c:out value="${contentCreationEvent.contributor.lastName}" />
                            </div>
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</content:section>

<content:aside>
    <h5 class="center"><fmt:message key="preview" /></h5>
    
    <audio 
        src="<spring:url value='/audio/${audio.id}.${fn:toLowerCase(audio.audioFormat)}' />"
        controls="true">
    </audio>
</content:aside>

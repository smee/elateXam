<script type="text/javascript">
    if (getCookie("username") != null) {
        $("j_username").value = getCookie("username");
        $("j_password").focus();
    } else {
        $("j_username").focus();
    }
    
    function saveUsername(theForm) {
        var expires = new Date();
        expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.
        setCookie("username",theForm.j_username.value,expires,"<c:url value="/"/>");
    }
    
    function validateForm(form) {                                                               
        return validateRequired(form); 
    } 
    /**
     * Add value of input field j_suffix to j_username iff it results into an email.
     * @param form
     * @returns
     */
    function adjustUsername(form){
    	if(form.j_suffix && containsAt(form.j_suffix.value) && !containsAt(form.j_username.value)){
    		form.j_username.value=form.j_username.value+form.j_suffix.value;
    	}
    }
    function containsAt(str){
    	return str.indexOf('@')>-1;
    }
    
    function passwordHint() {
        if ($("j_username").value.length == 0) {
            alert("The <fmt:message key="label.username"/> field must be filled in to get a password hint sent to you.");
            $("j_username").focus();
        } else {
            location.href="<c:url value="/passwordHint.html"/>?username=" + $("j_username").value;     
        }
    }
    
    function required () { 
        this.aa = new Array("j_username", "<fmt:message key="errors.required"><fmt:param><fmt:message key="label.username"/></fmt:param></fmt:message>", new Function ("varName", " return this[varName];"));
        this.ab = new Array("j_password", "<fmt:message key="errors.required"><fmt:param><fmt:message key="label.password"/></fmt:param></fmt:message>", new Function ("varName", " return this[varName];"));
    } 
</script>

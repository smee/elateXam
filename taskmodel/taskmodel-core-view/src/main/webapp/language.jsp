<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- RETYPE/TEXTRESET -->
<c:set var="inputLanguageEnabled" value="${false}"/>
<c:forEach items="${SubTasklets}" var="SubTasklet">
	<!-- Should we check for some default language, too? -->
	<c:if test="${SubTasklet.inputLanguage != null }"> 
		<c:set var="inputLanguageEnabled" value="${true}"/>
	</c:if>
</c:forEach>

<c:if test="${inputLanguageEnabled == true}">
	<!--
		Author: Martin Czygan <martin.czygan ~ gmail.com>

		jQuery (plugin) functionality
			1. jquery-resettable-textinput (reset textfields and textareas to initial values)
			2. jquery-retype (multilingual input in textfields and textareas)
			3. floating infobox for keyboard hints

		To use the a current version of the jQuery library, one can use Google's jsapi
		See also: http://googleajaxsearchapi.blogspot.com/2008/05/speed-up-access-to-your-favorite.html

		For now we stick to jQuery 1.4.2 which is the -current as of 2010-02-24
	-->
	<!-- JQuery Plugins loading and integration -->
	<!--
		The core framework. Find a current release on http://code.google.com/p/jqueryjs/downloads/list
	    or from http://docs.jquery.com/Downloading_jQuery
	-->
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery/jquery-1.4.2.min.js"></script>
	<!-- JQuery Textreset-->
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery/jquery-textreset/jquery.textreset.js"></script>
	<!-- JQuery Retype and dependency; http://myyn.org/retype -->
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery/jquery-retype-next/jquery.fieldselection.min.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery/jquery-retype-next/jquery.retype.min.js"></script>
	<!-- Floating infobox for language properties, dependencies, 4abd0468bdc87619cffa5bf7c35ec3b) -->
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery/jquery-ui-1.7.custom.min.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery/jquery.dimensions.js"></script>
	<!-- Cookies (infobox state memory)-->
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery/jquery.cookie.js"></script>
	<!-- CSS for language-infobox e4abd0468bdc87619cffa5bf7c35ec3b-->
	<link rel="stylesheet" href="<%= request.getContextPath() %>/jquery/jquery-retype-next/infobox.css"  
		type="text/css" media="screen" title="infobox-css" charset="utf-8">
	<!-- HTML box placeholder for infobox; keyboard hints from retype will be loaded into here -->
	<div id="infobox-language-container"><div id="infobox-language"></div></div>

	<script type="text/javascript">
		// The actual processing of textreset/retype/infobox on pages, which need their functionality.
		$(document).ready(function(){
			function installRetype(languageCode) {
				var mapping_url = "<%= request.getContextPath() %>" +
					"/jquery/jquery-retype-next/mappings/ialt_" + 
					languageCode + ".json";
				$("input[lang='" + languageCode + "']").retype( 
					"on", {"mapping_url" :  mapping_url });
				$("textarea[lang='" + languageCode + "']").retype(
					"on", {"mapping_url" : mapping_url });
			}
			// INFOBOX
			var infobox_div = "#infobox-language";
			var IB_COOKIE = "INFOBOX_COOKIE-7578a4df574f6d079bbd24a35ec1c097";
			var menuYloc = null;

			menuYloc = parseInt($(infobox_div).css("top").substring(
				0,$(infobox_div).css("top").indexOf("px")));
			$(window).scroll(function () {
				var offset = menuYloc+$(document).scrollTop() + "px";
				$(infobox_div).animate({ top:offset },{ duration:0, queue:false });
			});

			// hide on first run
			$(infobox_div).hide();

			// TEXTRESET
			var graphical_hint = "<img style='border: 0px'" + 
				" src='<%= request.getContextPath() %>" + 
				"/jquery/jquery-textreset/images/left38.gif'></img>";
			$("input[type='text']").textreset({ "linktext" : graphical_hint });
			$("textarea").textareareset({ "linktext" : graphical_hint });

			// RETYPE
			installRetype("ru");
			installRetype("fr");
			installRetype("es");

			// show appropriate infobox on focus 
			// (if we have a lanaguage tag in input)
			// RETYPE (onFocus event)
			$("input, textarea").focus(function() {
				$this = $(this);
				var language_code = $this.attr("lang");
				if (language_code != "ru" &&  
					language_code != "fr" && 
					language_code != "es") { 
					$(infobox_div).hide();
				} else {
					$(infobox_div).show().draggable();
					// somehow loadHelpText doesn't worked with pages 
					// called via location.href = '...';
					// so just get and replace the html snippet
					$.get(
						"<%= request.getContextPath() %>" + 
						"/jquery/jquery-retype-next/help/infobox_" + 
						language_code + ".html", 
						function(data) {
							$(infobox_div).html(data);
							$("#infobox-language-close").click(function() {
								$(".infobox-language-content").toggle();
								$.cookie(IB_COOKIE, 
									$(".infobox-language-content").css("display"), 
									{ expires: 7 });
							});
							// restore the display from cookie value
							$(".infobox-language-content").css("display", 
								$.cookie(IB_COOKIE));
					}); // (get)
				} // (else)
			}); // (input, textarea focus)
		}); // (jquery, infobox, textreset and retype stuff)
	</script>
</c:if>
<!-- END RETYPE/TEXTRESET -->

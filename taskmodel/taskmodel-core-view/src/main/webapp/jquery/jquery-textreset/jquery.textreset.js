/* 
	encoding: UTF-8 
	jquery.textreset.js
	date: 2009-02-11
	copyright: (c) 2008--2009 Martin Czygan < martin.czygan ~ gmail.com >  
*/
(function($) {
	$.fn.textreset = function(options) {
		
		var defaults = { 
			tooltip : "Wiederherstellen des Ausgangswertes", 
			wrap_in_div : false,
		 	linktext : "Reset", 
			identify_by : "name", 
			initial_value_from : "value"
		}
		options = $.extend({}, defaults, options);
		
		return this.each(function(){
			$this = $(this);
			if (!$this.attr("value") == "") {
				// store initial value
				$this.attr("initial-value", $this.attr(options.initial_value_from));
				
				var input_id = $this.attr(options.identify_by);
			
				var anchor = 'r-' + input_id
				var link = $("<a name='" + anchor + "' title='" + 
					options.tooltip + "' class='reset-link' input='" + input_id + 
					"' id='reset-" + input_id +  "' href='#" + anchor + "'>" + options.linktext + "</a>");
			
				link.click(function() {
					var id = $(this).attr("input");
					var iv = $("input[" + options.identify_by + "='" + id + "']").attr("initial-value");
					$("input[" + options.identify_by + "='" + id + "']").attr("value", iv);
					$("input[" + options.identify_by + "='" + id + "']").focus();
				});
			
				$this.after(link);
				if (options.wrap_in_div) {
					$("#" + input_id + ", #reset-" + input_id).wrapAll("<div class='reset-textfield-div'></div>")
				}
			}
		});
	}
	
	$.fn.textareareset = function(options) {
		
		var defaults = { 
			tooltip : "Reset textarea to initial value.", 
			wrap_in_div : false,
		  	linktext : "Reset", 
			identify_by : "name", // uniq id
		}
		options = $.extend({}, defaults, options);
		
		return this.each(function(){
			$this = $(this);
			if (!$this.html() == "") {
				// store initial value
			
				$this.attr("initial-value", $this.text());
				
				var textarea_id = $this.attr(options.identify_by);
			
				var anchor = 'r-' + textarea_id
				var link = $("<a title='" + 
					options.tooltip + "' class='reset-link' input='" + textarea_id + 
					"' id='reset-" + textarea_id +  "' href='#" + anchor + "'>" + options.linktext +"</a>");
			
				link.click(function() {
					var id = $(this).attr("input");
					var iv = $("textarea[" + options.identify_by + "='" + id + "']").attr("initial-value");
					$("textarea[" + options.identify_by + "='" + id + "']").val(iv);
					$("textarea[" + options.identify_by + "='" + id + "']").focus();
				});
			
				$this.after(link);
				if (options.wrap_in_div) {
					$("#" + textarea_id + ", #reset-" + textarea_id).wrapAll("<div class='reset-textfield-div'></div>")
				}
			}
		});
	}

// end of closure
})(jQuery);

// EOF

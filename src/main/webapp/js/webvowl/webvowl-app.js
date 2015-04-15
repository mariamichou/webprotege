// Source: src/js/app/header.js
"use strict";

var webvowlApp = webvowlApp || {};

// Source: src/js/app/app.js
webvowlApp.app = function () {

	var app = {},
		graph = webvowl.graph(),
		options = graph.graphOptions(),
		graphSelector = "#graph",
		jsonBasePath = "js/data/",
		defaultJsonName = "foaf", // This file is loaded by default
	// Modules for the webvowl app
		exportMenu,
		gravityMenu,
		filterMenu,
		modeMenu,
		resetMenu,
		pauseMenu,
		setupableMenues,
	// Graph modules
		statistics = webvowl.modules.statistics(),
		focuser = webvowl.modules.focuser(),
		datatypeFilter = webvowl.modules.datatypeFilter(),
		subclassFilter = webvowl.modules.subclassFilter(),
		disjointFilter = webvowl.modules.disjointFilter(),
		nodeDegreeFilter = webvowl.modules.nodeDegreeFilter(),
		setOperatorFilter = webvowl.modules.setOperatorFilter(),
		nodeScalingSwitch = webvowl.modules.nodeScalingSwitch(graph),
		pickAndPin = webvowl.modules.pickAndPin(),
	// Selections for the app
		loadingError = d3.select("#loading-error"),
		loadingProgress = d3.select("#loading-progress");

	app.initialize = function () {
		options.graphContainerSelector(graphSelector);
		options.selectionModules().push(focuser);
		options.selectionModules().push(pickAndPin);
		options.filterModules().push(statistics);
		options.filterModules().push(datatypeFilter);
		options.filterModules().push(subclassFilter);
		options.filterModules().push(disjointFilter);
		options.filterModules().push(setOperatorFilter);
		options.filterModules().push(nodeScalingSwitch);
		options.filterModules().push(nodeDegreeFilter);

		loadOntology("js/data/foaf.json");

		exportMenu = webvowlApp.exportMenu(options.graphContainerSelector());
		gravityMenu = webvowlApp.gravityMenu(graph);
		filterMenu = webvowlApp.filterMenu(graph, datatypeFilter, subclassFilter, disjointFilter, setOperatorFilter, nodeDegreeFilter);
		modeMenu = webvowlApp.modeMenu(graph, pickAndPin, nodeScalingSwitch);
		pauseMenu = webvowlApp.pauseMenu(graph);
		resetMenu = webvowlApp.resetMenu(graph, [gravityMenu, filterMenu, modeMenu,
			focuser, pauseMenu]);

		d3.select(window).on("resize", adjustSize);

		// setup all bottom bar modules
		setupableMenues = [exportMenu, gravityMenu, filterMenu, modeMenu, resetMenu, pauseMenu];
		setupableMenues.forEach(function (menu) {
			menu.setup();
		});

		graph.start();
		adjustSize();
	};

	function loadOntology(relativePath) {
		loadingError.classed("hidden", true);
		loadingProgress.classed("hidden", false);

		d3.xhr(relativePath, 'application/json', function (error, request) {
			pauseMenu.reset();

			var loadingFailed = !!error;
			if (loadingFailed) {
				d3.select("#custom-error-message").text(error.response || "");
			}
			loadingError.classed("hidden", !loadingFailed);
			loadingProgress.classed("hidden", true);

			var jsonText = request.responseText,
				data = JSON.parse(jsonText);

			exportMenu.setJsonText(jsonText);

			options.data(data);
			graph.reload();

			var filename = relativePath.slice(relativePath.lastIndexOf("/") + 1);
			exportMenu.setFilename(filename.split(".")[0]);
		});
	}

	function adjustSize() {
		var graphContainer = d3.select(graphSelector),
			svg = graphContainer.select("svg"),
			height = window.innerHeight - 40,
			width = window.innerWidth - (window.innerWidth * 0.22);

		graphContainer.style("height", height + "px");
		svg.attr("width", width)
			.attr("height", height);

		options.width(width)
			.height(height);
		graph.updateStyle();
	}

	function setupConverterButton() {
		function setActionAttribute() {
			d3.select(".converter-form").attr("action", "#iri=" + d3.select("#convert-iri").property("value"));
		}

		// Call it initially because there might be a value already in the input field
		setActionAttribute();
		d3.select("#convert-iri").on("change", function() {
			setActionAttribute();
		});
	}

	return app;
};

// Source: src/js/app/browserWarning.js
/* Taken from here: http://stackoverflow.com/a/17907562 */
function getInternetExplorerVersion() {
	var ua,
		re,
		rv = -1;
	if (navigator.appName === "Microsoft Internet Explorer") {
		ua = navigator.userAgent;
		re = new RegExp("MSIE ([0-9]{1,}[\\.0-9]{0,})");
		if (re.exec(ua) !== null)
			rv = parseFloat(RegExp.$1);
	} else if (navigator.appName === "Netscape") {
		ua = navigator.userAgent;
		re = new RegExp("Trident/.*rv:([0-9]{1,}[\\.0-9]{0,})");
		if (re.exec(ua) !== null)
			rv = parseFloat(RegExp.$1);
	}
	return rv;
}

var version = getInternetExplorerVersion();
if (version > 0 && version <= 11) {
	document.write("<div id=\"browserCheck\">The WebVOWL demo does not work in Internet Explorer. Please use another browser, such as <a href=\"http://www.mozilla.org/firefox/\">Mozilla Firefox</a> or <a href=\"https://www.google.com/chrome/\">Google Chrome</a>, to run the WebVOWL demo.</div>");
	// hiding any additional menus and features
	var canvasArea = document.getElementById("canvasArea"),
		detailsArea = document.getElementById("detailsArea"),
		optionsArea = document.getElementById("optionsArea");
	canvasArea.className = "hidden";
	detailsArea.className = "hidden";
	optionsArea.className = "hidden";
}

// Source: src/js/app/menu/exportMenu.js
/**
 * Contains the logic for the export button.
 *
 * @param graphSelector the associated graph svg selector
 * @returns {{}}
 */
webvowlApp.exportMenu = function (graphSelector) {

	var exportMenu = {},
		exportSvgButton,
		exportFilename,
		exportJsonButton,
		exportableJsonText;


	/**
	 * Adds the export button to the website.
	 */
	exportMenu.setup = function () {
		exportSvgButton = d3.select("#exportSvg")
			.on("click", exportSvg);
		exportJsonButton = d3.select("#exportJson")
			.on("click", exportJson);
	};

	exportMenu.setFilename = function (filename) {
		exportFilename = filename;
	};

	exportMenu.setJsonText = function (jsonText) {
		exportableJsonText = jsonText;
	};

	function exportSvg() {
		// Get the d3js SVG element
		var graphSvg = d3.select(graphSelector).select("svg"),
			graphSvgCode,
			escapedGraphSvgCode,
			dataURI;

		// inline the styles, so that the exported svg code contains the css rules
		inlineVowlStyles();
		hideNonExportableElements();

		graphSvgCode = graphSvg.attr("version", 1.1)
			.attr("xmlns", "http://www.w3.org/2000/svg")
			.node().parentNode.innerHTML;

		// Insert the reference to VOWL
		graphSvgCode = "<!-- Created with WebVOWL (version " + webvowlApp.version + ")" +
		", http://vowl.visualdataweb.org -->\n" + graphSvgCode;

		escapedGraphSvgCode = escapeUnicodeCharacters(graphSvgCode);
		//btoa(); Creates a base-64 encoded ASCII string from a "string" of binary data.
		dataURI = "data:image/svg+xml;base64," + btoa(escapedGraphSvgCode);

		exportSvgButton.attr("href", dataURI)
			.attr("download", exportFilename + ".svg");

		// remove graphic styles for interaction to go back to normal
		removeVowlInlineStyles();
		showNonExportableElements();
	}

	function escapeUnicodeCharacters(text) {
		var textSnippets = [],
			i, textLength = text.length,
			character,
			charCode;

		for (i = 0; i < textLength; i++) {
			character = text.charAt(i);
			charCode = character.charCodeAt(0);

			if (charCode < 255) {
				textSnippets.push(character);
			} else {
				textSnippets.push("&#" + charCode + ";");
			}
		}

		return textSnippets.join("");
	}

	function inlineVowlStyles() {
		d3.selectAll(".text").style("font-family", "Helvetica, Arial, sans-serif").style("font-size", "12px");
		d3.selectAll(".subtext").style("font-size", "9px");
		d3.selectAll(".cardinality").style("font-size", "10px");
		d3.selectAll(".text, .embedded").style("pointer-events", "none");
		d3.selectAll(".class, .object, .disjoint, .objectproperty, .disjointwith, .equivalentproperty, .transitiveproperty, .functionalproperty, .inversefunctionalproperty, .symmetricproperty").style("fill", "#acf");
		d3.selectAll(".label .datatype, .datatypeproperty").style("fill", "#9c6");
		d3.selectAll(".rdf, .rdfproperty").style("fill", "#c9c");
		d3.selectAll(".literal, .node .datatype").style("fill", "#fc3");
		d3.selectAll(".deprecated, .deprecatedproperty").style("fill", "#ccc");
		d3.selectAll(".external, .externalproperty").style("fill", "#36c");
		d3.selectAll("path, .nofill").style("fill", "none");
		d3.selectAll(".symbol").style("fill", "#69c");
		d3.selectAll(".arrowhead, marker path").style("fill", "#000");
		d3.selectAll(".class, path, line, .fineline").style("stroke", "#000");
		d3.selectAll(".white, .subclass, .dottedMarker path, .subclassproperty, .external + text").style("fill", "#fff");
		d3.selectAll(".class.hovered, .property.hovered, path.arrowhead.hovered, .cardinality.hovered, .normalMarker path.hovered, .cardinality.focused, .normalMarker path.focused, circle.pin").style("fill", "#f00").style("cursor", "pointer");
		d3.selectAll(".focused, path.hovered").style("stroke", "#f00");
		d3.selectAll(".label .indirectHighlighting, .feature:hover").style("fill", "#f90");
		d3.selectAll(".class, path, line").style("stroke-width", "2");
		d3.selectAll(".fineline").style("stroke-width", "1");
		d3.selectAll(".special").style("stroke-dasharray", "8");
		d3.selectAll(".dotted").style("stroke-dasharray", "3");
		d3.selectAll("rect.focused, circle.focused").style("stroke-width", "4px");
		d3.selectAll(".nostroke").style("stroke", "none");
		d3.selectAll("#width-test").style("position", "absolute").style("float", "left").style("white-space", "nowrap").style("visibility", "hidden");
		d3.selectAll("marker path").style("stroke-dasharray", "50");
	}

	/**
	 * For example the pin of the pick&pin module should be invisible in the exported graphic.
	 */
	function hideNonExportableElements() {
		d3.selectAll(".hidden-in-export").style("display", "none");
	}

	function removeVowlInlineStyles() {
		d3.selectAll(".text, .subtext, .cardinality, .text, .embedded, .class, .object, .disjoint, .objectproperty, .disjointwith, .equivalentproperty, .transitiveproperty, .functionalproperty, .inversefunctionalproperty, .symmetricproperty, .label .datatype, .datatypeproperty, .rdf, .rdfproperty, .literal, .node .datatype, .deprecated, .deprecatedproperty, .external, .externalproperty, path, .nofill, .symbol, .arrowhead, marker path, .class, path, line, .fineline, .white, .subclass, .dottedMarker path, .subclassproperty, .external + text, .class.hovered, .property.hovered, path.arrowhead.hovered, .cardinality.hovered, .normalMarker path.hovered, .cardinality.focused, .normalMarker path.focused, circle.pin, .focused, path.hovered, .label .indirectHighlighting, .feature:hover, .class, path, line, .fineline, .special, .dotted, rect.focused, circle.focused, .nostroke, #width-test, marker path").attr("style", null);
	}

	function showNonExportableElements() {
		d3.selectAll(".hidden-in-export").style("display", null);
	}

	function exportJson() {
		if (!exportableJsonText) {
			alert("No graph data available.");
			// Stop the redirection to the path of the href attribute
			d3.event.preventDefault();
			return;
		}

		var dataURI = "data:text/json;charset=utf-8," + encodeURIComponent(exportableJsonText);
		exportJsonButton.attr("href", dataURI)
			.attr("download", exportFilename + ".json");
	}

	return exportMenu;
};

// Source: src/js/app/menu/filterMenu.js
/**
 * Contains the logic for connecting the filters with the website.
 *
 * @param graph required for calling a refresh after a filter change
 * @param datatypeFilter filter for all datatypes
 * @param subclassFilter filter for all subclasses
 * @param disjointFilter filter for all disjoint with properties
 * @param setOperatorFilter filter for all set operators with properties
 * @param nodeDegreeFilter filters nodes by their degree
 * @returns {{}}
 */
webvowlApp.filterMenu = function (graph, datatypeFilter, subclassFilter, disjointFilter, setOperatorFilter, nodeDegreeFilter) {

	var filterMenu = {},
		checkboxData = [],
		degreeSlider;


	/**
	 * Connects the website with graph filters.
	 */
	filterMenu.setup = function () {
		addFilterItem(datatypeFilter, "datatype", "Datatype prop.", "#datatypeFilteringOption");
		addFilterItem(subclassFilter, "subclass", "Solitary subclass.", "#subclassFilteringOption");
		addFilterItem(disjointFilter, "disjoint", "Disjointness info", "#disjointFilteringOption");
		addFilterItem(setOperatorFilter, "setoperator", "Set operators", "#setOperatorFilteringOption");

		addNodeDegreeFilter("#nodeDegreeFilteringOption");
	};


	function addFilterItem(filter, identifier, pluralNameOfFilteredItems, selector) {
		var filterContainer,
			filterCheckbox;

		filterContainer = d3.select(selector)
			.append("div")
			.classed("checkboxContainer", true);

		filterCheckbox = filterContainer.append("input")
			.classed("filterCheckbox", true)
			.attr("id", identifier + "FilterCheckbox")
			.attr("type", "checkbox")
			.property("checked", filter.enabled());

		// Store for easier resetting
		checkboxData.push({checkbox: filterCheckbox, defaultState: filter.enabled()});

		filterCheckbox.on("click", function () {
			// There might be no parameters passed because of a manual
			// invocation when resetting the filters
			var isEnabled = filterCheckbox.property("checked");
			filter.enabled(isEnabled);
			graph.update();
		});

		filterContainer.append("label")
			.attr("for", identifier + "FilterCheckbox")
			.text(pluralNameOfFilteredItems);
	}

	function addNodeDegreeFilter(selector) {
		nodeDegreeFilter.setMaxDegreeSetter(function(maxDegree) {
			degreeSlider.attr("max", maxDegree);
			degreeSlider.property("value", Math.min(maxDegree, degreeSlider.property("value")));
		});

		nodeDegreeFilter.setDegreeQueryFunction(function () {
			return degreeSlider.property("value");
		});

		var sliderContainer,
			sliderValueLabel;

		sliderContainer = d3.select(selector)
			.append("div")
			.classed("distanceSliderContainer", true);

		degreeSlider = sliderContainer.append("input")
			.attr("id", "nodeDegreeDistanceSlider")
			.attr("type", "range")
			.attr("min", 0)
			.attr("step", 1);

		sliderContainer.append("label")
			.classed("description", true)
			.attr("for", "nodeDegreeDistanceSlider")
			.text("Degree of collapsing");

		sliderValueLabel = sliderContainer.append("label")
			.classed("value", true)
			.attr("for", "nodeDegreeDistanceSlider")
			.text(0);

		degreeSlider.on("change", function () {
			var degree = degreeSlider.property("value");
			sliderValueLabel.text(degree);
			graph.update();
		});
	}

	/**
	 * Resets the filters (and also filtered elements) to their default.
	 */
	filterMenu.reset = function () {
		checkboxData.forEach(function (checkboxData) {
			var checkbox = checkboxData.checkbox,
				enabledByDefault = checkboxData.defaultState,
				isChecked = checkbox.property("checked");

			if (isChecked !== enabledByDefault) {
				checkbox.property("checked", enabledByDefault);
				// Call onclick event handlers programmatically
				checkbox.on("click")();
			}
		});

		degreeSlider.property("value", 0);
		degreeSlider.on("change")();
	};


	return filterMenu;
};

// Source: src/js/app/menu/gravityMenu.js
/**
 * Contains the logic for setting up the gravity sliders.
 *
 * @param graph the associated webvowl graph
 * @returns {{}}
 */
webvowlApp.gravityMenu = function (graph) {

	var gravityMenu = {},
		sliders = [],
		options = graph.graphOptions(),
		defaultCharge = options.charge(),
		defaultLinkDistance = options.defaultLinkDistance();


	/**
	 * Adds the gravity sliders to the website.
	 */
	gravityMenu.setup = function () {
		addDistanceSlider("#classSliderOption", "class", "Class distance", options.classDistance);
		addDistanceSlider("#datatypeSliderOption", "datatype", "Datatype distance", options.datatypeDistance);
	};

	function addDistanceSlider(selector, identifier, label, distanceFunction) {
		var sliderContainer,
			sliderValueLabel;

		sliderContainer = d3.select(selector)
			.append("div")
			.datum({distanceFunction: distanceFunction}) // connect the options-function with the slider
			.classed("distanceSliderContainer", true);

		var slider = sliderContainer.append("input")
			.attr("id", identifier + "DistanceSlider")
			.attr("type", "range")
			.attr("min", 10)
			.attr("max", 600)
			.attr("value", distanceFunction())
			.attr("step", 10);

		sliderContainer.append("label")
			.classed("description", true)
			.attr("for", identifier + "DistanceSlider")
			.text(label);

		sliderValueLabel = sliderContainer.append("label")
			.classed("value", true)
			.attr("for", identifier + "DistanceSlider")
			.text(distanceFunction());

		// Store slider for easier resetting
		sliders.push(slider);

		slider.on("input", function () {
			var distance = slider.property("value");
			distanceFunction(distance);
			adjustCharge();
			sliderValueLabel.text(distance);
			graph.updateStyle();
		});
	}

	function adjustCharge() {
		var greaterDistance = Math.max(options.classDistance(), options.datatypeDistance()),
			ratio = greaterDistance / defaultLinkDistance,
			newCharge = defaultCharge * ratio;

		options.charge(newCharge);
	}

	/**
	 * Resets the gravity sliders to their default.
	 */
	gravityMenu.reset = function () {
		sliders.forEach(function (slider) {
			slider.property("value", function (d) {
				// Simply reload the distance from the options
				return d.distanceFunction();
			});
			slider.on("input")();
		});
	};


	return gravityMenu;
};

// Source: src/js/app/menu/modeMenu.js
/**
 * Contains the logic for connecting the modes with the website.
 *
 * @param graph the graph that belongs to these controls
 * @param pickAndPin mode for picking and pinning of nodes
 * @param nodeScaling mode for toggling node scaling
 * @returns {{}}
 */
webvowlApp.modeMenu = function (graph, pickAndPin, nodeScaling) {

	var modeMenu = {},
		checkboxes = [];


	/**
	 * Connects the website with the available graph modes.
	 */
	modeMenu.setup = function () {
		addModeItem(pickAndPin, "pickandpin", "Pick & Pin", "#pickAndPinOption", false);
		addModeItem(nodeScaling, "nodescaling", "Node Scaling", "#nodeScalingOption", true);
	};

	function addModeItem(module, identifier, modeName, selector, updateGraphOnClick) {
		var moduleOptionContainer,
			moduleCheckbox;

		moduleOptionContainer = d3.select(selector)
			.append("div")
			.classed("checkboxContainer", true)
			.datum({module: module, defaultState: module.enabled()});

		moduleCheckbox = moduleOptionContainer.append("input")
			.classed("moduleCheckbox", true)
			.attr("id", identifier + "ModuleCheckbox")
			.attr("type", "checkbox")
			.property("checked", module.enabled());

		// Store for easier resetting all modes
		checkboxes.push(moduleCheckbox);

		moduleCheckbox.on("click", function (d) {
			var isEnabled = moduleCheckbox.property("checked");
			d.module.enabled(isEnabled);

			if (updateGraphOnClick) {
				graph.update();
			}
		});

		moduleOptionContainer.append("label")
			.attr("for", identifier + "ModuleCheckbox")
			.text(modeName);
	}

	/**
	 * Resets the modes to their default.
	 */
	modeMenu.reset = function () {
		checkboxes.forEach(function (checkbox) {
			var defaultState = checkbox.datum().defaultState,
				isChecked = checkbox.property("checked");

			if (isChecked !== defaultState) {
				checkbox.property("checked", defaultState);
				// Call onclick event handlers programmatically
				checkbox.on("click")(checkbox.datum());
			}

			// Reset the module that is connected with the checkbox
			checkbox.datum().module.reset();
		});
	};


	return modeMenu;
};

// Source: src/js/app/menu/pauseMenu.js
/**
 * Contains the logic for the pause and resume button.
 *
 * @param graph the associated webvowl graph
 * @returns {{}}
 */
webvowlApp.pauseMenu = function (graph) {

	var pauseMenu = {},
		pauseButton;


	/**
	 * Adds the pause button to the website.
	 */
	pauseMenu.setup = function () {
		pauseButton = d3.select("#pauseOption")
			.append("a")
			.datum({paused: false})
			.attr("id", "pause")
			.attr("href", "#")
			.on("click", function (d) {
				if (d.paused) {
					graph.unfreeze();
				} else {
					graph.freeze();
				}
				d.paused = !d.paused;
				updatePauseButton();
			});

		// Set these properties the first time manually
		updatePauseButton();
	};

	function updatePauseButton() {
		updatePauseButtonClass();
		updatePauseButtonText();
	}

	function updatePauseButtonClass() {
		pauseButton.classed("paused", function (d) {
			return d.paused;
		});
	}

	function updatePauseButtonText() {
		if (pauseButton.datum().paused) {
			pauseButton.text("Resume");
		} else {
			pauseButton.text("Pause");
		}
	}

	pauseMenu.reset = function() {
		// Simulate resuming
		pauseButton.datum().paused = false;
		graph.unfreeze();
		updatePauseButton();
	};


	return pauseMenu;
};

// Source: src/js/app/menu/resetMenu.js
/**
 * Contains the logic for the reset button.
 *
 * @param graph the associated webvowl graph
 * @param resettableModules modules that can be resetted
 * @returns {{}}
 */
webvowlApp.resetMenu = function (graph, resettableModules) {

	var resetMenu = {},
		options = graph.graphOptions(),
		untouchedOptions = webvowl.options();


	/**
	 * Adds the reset button to the website.
	 */
	resetMenu.setup = function () {
		d3.select("#resetOption")
			.append("a")
			.attr("id", "reset")
			.attr("href", "#")
			.property("type", "reset")
			.text("Reset")
			.on("click", resetGraph);
	};

	function resetGraph() {
		options.classDistance(untouchedOptions.classDistance());
		options.datatypeDistance(untouchedOptions.datatypeDistance());
		options.charge(untouchedOptions.charge());
		options.gravity(untouchedOptions.gravity());
		options.linkStrength(untouchedOptions.linkStrength());
		graph.reset();

		resettableModules.forEach(function (module) {
			module.reset();
		});

		graph.updateStyle();
	}


	return resetMenu;
};

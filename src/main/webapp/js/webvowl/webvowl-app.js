// Source: src/js/app/header.js
"use strict";

var webvowlApp = webvowlApp || {};
webvowlApp.version = "0.4.0";

// Source: src/js/app/app.js
webvowlApp.app = function (graphContainerSelector, convertedOntology) {

	var app = {},
		graph = webvowl.graph(),
		options = graph.graphOptions(),
		languageTools = webvowl.util.languageTools(),
		cachedConversions = {},
	// Graph modules
		statistics = webvowl.modules.statistics(),
		focuser = webvowl.modules.focuser(),
		datatypeFilter = webvowl.modules.datatypeFilter(),
		subclassFilter = webvowl.modules.subclassFilter(),
		disjointFilter = webvowl.modules.disjointFilter(),
		nodeDegreeFilter = webvowl.modules.nodeDegreeFilter(),
		setOperatorFilter = webvowl.modules.setOperatorFilter(),
		nodeScalingSwitch = webvowl.modules.nodeScalingSwitch(graph),
		compactNotationSwitch = webvowl.modules.compactNotationSwitch(graph),
		pickAndPin = webvowl.modules.pickAndPin();

	app.initialize = function () {
		options.graphContainerSelector(graphContainerSelector);
		options.selectionModules().push(focuser);
		options.selectionModules().push(pickAndPin);
		options.filterModules().push(statistics);
		options.filterModules().push(datatypeFilter);
		options.filterModules().push(subclassFilter);
		options.filterModules().push(disjointFilter);
		options.filterModules().push(setOperatorFilter);
		options.filterModules().push(nodeScalingSwitch);
		options.filterModules().push(nodeDegreeFilter);
		options.filterModules().push(compactNotationSwitch);

		graph.start();

		adjustSize();
		d3.select(window).on("resize", adjustSize);

		loadOntologyFromText(convertedOntology);
	};

	function loadOntologyFromText(jsonText) {
		var data;
		if (jsonText) {
			data = JSON.parse(jsonText);
		}

		options.data(data);
		graph.reload();
	}

	function adjustSize() {
		var graphContainer = d3.select(graphContainerSelector),
			svg = graphContainer.select("svg"),
			height = parseInt(graphContainer.style("height"), 10),
			width = parseInt(graphContainer.style("width"), 10);

		svg.attr("width", width)
			.attr("height", height);

		options.width(width)
			.height(height);
		graph.updateStyle();
	}

	return app;
};

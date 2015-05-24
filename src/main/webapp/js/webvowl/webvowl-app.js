// Source: src/js/app/header.js
"use strict";

var webvowlApp = webvowlApp || {};
webvowlApp.version = "0.4.0";

// Source: src/js/app/app.js
webvowlApp.app = function (graphContainerSelector) {

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

		loadOntologyFromUri("/js/data/foaf.json", "/js/data/foaf.json");
	};

	function loadOntologyFromUri(relativePath, requestedUri) {
		var cachedOntology = cachedConversions[relativePath];
		var trimmedRequestedUri = requestedUri.replace(/\/$/g, "");
		var filename = trimmedRequestedUri.slice(trimmedRequestedUri.lastIndexOf("/") + 1);

		if (cachedOntology) {
			loadOntologyFromText(cachedOntology, undefined, filename);
		} else {
			//displayLoadingIndicators(); TODO
			d3.xhr(relativePath, "application/json", function (error, request) {
				var loadingSuccessful = !error;

				var jsonText;
				if (loadingSuccessful) {
					jsonText = request.responseText;
					cachedConversions[relativePath] = jsonText;
				}

				loadOntologyFromText(jsonText, undefined, filename);
			});
		}
	}

	function loadOntologyFromText(jsonText, filename, alternativeFilename) {
		var data;
		if (jsonText) {
			data = JSON.parse(jsonText);

			if (!filename) {
				// First look if an ontology title exists, otherwise take the alternative filename
				var ontologyNames = data.header ? data.header.title : undefined;
				var ontologyName = languageTools.textForCurrentLanguage(ontologyNames);

				if (ontologyName) {
					filename = ontologyName;
				} else {
					filename = alternativeFilename;
				}
			}
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

package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

public class VOWLLabelJso extends VOWLElementJso {
	protected VOWLLabelJso() {
	}

	public final native VOWLNodeJso getDomain() /*-{
	  return this.domain();
  }-*/;

	public final native VOWLNodeJso getRange() /*-{
	  return this.range()
  }-*/;

}

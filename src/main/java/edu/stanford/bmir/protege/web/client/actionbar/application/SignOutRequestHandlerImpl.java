package edu.stanford.bmir.protege.web.client.actionbar.application;

import com.google.gwt.user.client.Window;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.ui.ontology.discussions.UserData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignOutRequestHandlerImpl implements SignOutRequestHandler {

    @Override
    public void handleSignOutRequest() {
    	UserData.setUserLogoutTime(Application.get().getUserId());
    	Window.alert("User " + Application.get().getUserId()+ " is logging out... time "+ UserData.getUserLogoutTime(Application.get().getUserId()) );
        Application.get().doLogOut();
    }
}

package edu.stanford.bmir.protege.web.client.ui.ontology.discussions;

import java.sql.Timestamp;
import java.util.HashMap;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import edu.stanford.bmir.protege.web.shared.user.UserId;

public class UserData {
	public static final HashMap<UserId, Timestamp> userData = new HashMap<UserId, Timestamp>();
	//private static ImmutableMap<UserId, Timestamp> userMap;
	//static ImmutableMap.Builder<UserId, Timestamp> builder = ImmutableMap.builder();
	
	public static final Optional<Timestamp> getUserLogoutTime(UserId userId) {
		return userData.get(userId)==null?Optional.<Timestamp>absent():Optional.of(userData.get(userId));
	}
	
	public static void setUserLogoutTime(UserId userId) {
		java.util.Date date= new java.util.Date();
        Timestamp t = new Timestamp(date.getTime());
		userData.put(userId, t);
		//builder.put(userId, t);
		//userMap = builder.build();
	}
	
	/*public static Timestamp getUserLogoutTime2(UserId userId) {
		return userMap.get(userId);
	}*/
	
}


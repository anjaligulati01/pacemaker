package controllers

import java.util.UUID;
import models.Activity
import models.Location
import models.User

class PacemakerAPI {

  var userIndex = hashMapOf<String, User>()
  var emailIndex = hashMapOf<String, User>()
  var activitiesIndex = hashMapOf<String, Activity>()
  var users = userIndex.values

  fun createUser(firstName: String, lastName: String, email: String, password: String): User {
    var user = User(firstName, lastName, email, password)
    userIndex[user.id] = user
    emailIndex[user.email] = user
    return user
  }

  fun deleteUsers() {
    userIndex.clear();
    emailIndex.clear()
  }

  fun getUser(id: String) = userIndex[id]
  fun getUserByEmail(email: String) = emailIndex[email]
	
	fun createActivity(id: String, type: String, location: String, distance: Float): Activity? {
    var activity:Activity? = null
    var user = userIndex.get(id)
    if (user != null) {
      activity = Activity(type, location, distance)
      user.activities[activity.id] = activity
      activitiesIndex[activity.id] = activity;
    }
    return activity;
  }

  fun getActivity(id: String): Activity? {
    return activitiesIndex[id]
  }
	
	fun getActivities(id: String): List<Activity>? {
    var user = userIndex.get(id)
		var activities:List<Activity>? = null
    if (user != null) {
        activities = user.activities.values.toList();
        //activities = activities.sortedWith(compareBy(Activity::type))
    }
    return activities;
  }

  fun deleteActivities(id: String) {
    require(userIndex[id] != null)
    var user = userIndex.get(id)
    if (user != null) {
      for ((u, activity) in user.activities) {
        activitiesIndex.remove(activity.id)
      }
      user.activities.clear();
    }
  }
	
	fun addLocation(id: String, latitude: Double, longitude: Double){
		require(activitiesIndex[id] != null)
		var activity = activitiesIndex.get(id)
		if(activity != null){
			val location = Location(latitude, longitude)
			activity.route.add(location);
		}
	}
	
	fun getActivityLocations(id: String): MutableList<Location>? {
		require(activitiesIndex[id] != null)
		var activity = activitiesIndex.get(id)
		var locations:MutableList<Location>? = null
		if(activity != null){
			locations = activity.route
		}
		return locations;
	}
	
	fun followFriend(id: String, email: String) {
    require(userIndex[id] != null)
    var user = userIndex.get(id)
    if (user != null) {
      user.friends.add(email)
    }
  }
	
	fun unfollowFriend(id: String, email: String) {
    require(userIndex[id] != null)
    var user = userIndex.get(id)
    if (user != null) {
      user.friends.remove(email)
    }
  }
	
	fun listFriends(id: String): MutableList<User>? {
		println("ENTER listFriends: " + id);
    var friendsList:MutableList<User>? = mutableListOf()
		require(userIndex[id] != null)
    var user = userIndex[id]
		if (user != null) {
      user.friends.forEach{friendsList?.add(getUserByEmail(it)!!)}
    }
		return friendsList;
  }
	
	fun getActivityReport(id: String): List<Activity>? {
		var user = userIndex.get(id)
		var activities:List<Activity>? = null
    if (user != null) {
        activities = user.activities.values.toList();
        activities = activities.sortedWith(compareBy(Activity::type))
    }
    return activities;
	}
	
	fun getFriendActivityReport(id: String, email:String): List<Activity>? {
		var user = userIndex.get(id)
		var activities:List<Activity>? = null
    if (user != null) {
      if(user.friends.contains(email)){
        var friendUser = getUserByEmail(email)
        if(friendUser != null){
          activities = friendUser.activities.values.toList();
          activities = activities.sortedWith(compareBy(Activity::type))
        }
      } 
    }
    return activities;
	}
	
}
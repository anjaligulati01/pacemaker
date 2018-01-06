package controllers

import io.javalin.Context
import models.Activity
import models.Location
import models.User

class PacemakerRestService  {
  val pacemaker = PacemakerAPI()

  fun listUsers(ctx: Context) {
    ctx.json(pacemaker.users)
  }

  fun createUser(ctx: Context) {
	  println("ENTER createUser: ");
    val user = ctx.bodyAsClass(User::class.java)
    val newUser = pacemaker.createUser(user.firstname, user.lastname, user.email, user.password)
    ctx.json(newUser)
  }

  fun deleteUsers(ctx: Context) {
	  println("ENTER deleteUsers: ");
    pacemaker.deleteUsers()
    ctx.status(200)
  }
	
	 fun getActivity(ctx: Context) {
		 
    // val userId: String? = ctx.param("id")
    val activityId: String? = ctx.param("activityId")
		 println("ENTER getActivity: " + activityId); 
    val activity = pacemaker.getActivity(activityId!!)
    if (activity != null) {
      ctx.json(activity)
    } else {
      ctx.status(404)
    }
  }

  fun getActivities(ctx: Context) {
    val id: String? =  ctx.param("id")
	  println("ENTER getActivities: " + id); 
    val user = pacemaker.getUser(id!!)
	  //println(" getActivities :: " + user!!)
    if (user != null) {
      ctx.json(pacemaker.getActivities(id)!!)
    } else {
      ctx.status(404)
    }
  }

   fun createActivity(ctx: Context) {
    val id: String? =  ctx.param("id")
	    println("ENTER createActivity: " + id); 
    val user = pacemaker.getUser(id!!)
    if (user != null) {
      val activity = ctx.bodyAsClass(Activity::class.java)
      val newActivity = pacemaker.createActivity(user.id, activity.type, activity.location, activity.distance)
      ctx.json(newActivity!!)
    } else {
      ctx.status(404)
    }
  }

  fun deleteActivites(ctx: Context) {
    val id: String? =  ctx.param("id")
    pacemaker.deleteActivities(id!!);
    ctx.status(204)
  }
	
	fun addLocation(ctx: Context) {
		val id: String? = ctx.param("id")
    val activityid: String? =  ctx.param("activityId")
		val user = pacemaker.getUser(id!!)
		if (user != null && activityid != null) {
			
		  val location = ctx.bodyAsClass(Location::class.java)
			pacemaker.addLocation(activityid, location.latitude, location.longitude)
		  ctx.status(204)
		}
  }
	
	fun getActivityLocations(ctx: Context) {
    val activityid: String? =  ctx.param("activityId")
		if (activityid != null) {
		  ctx.json(pacemaker.getActivityLocations(activityid)!!)
		}else{
			ctx.status(404)
		}
  }
	
	fun followFriend(ctx: Context) {
    val id: String? =  ctx.param("id")
		val email: String? = ctx.param("email")
		println("Enter followFriend" + id)
      pacemaker.followFriend(id!!, email!!);
    ctx.status(204)
  }
	
	fun unfollowFriend(ctx: Context) {
    val id: String? =  ctx.param("id")
		val email: String? = ctx.param("email")
      pacemaker.unfollowFriend(id!!, email!!);
    ctx.status(204)
  }
	
	fun listFriends(ctx: Context) {
    val id: String? =  ctx.param("id")
		val user = pacemaker.getUser(id!!)
		println("ListFriends: "+ user)
    if (user != null) {
      ctx.json(pacemaker.listFriends(id)!!);
    } else {
      ctx.status(404)
    }
  }
	
	fun getActivityReport(ctx: Context) {
    val id: String? =  ctx.param("id")
		val user = pacemaker.getUser(id!!)
    if (user != null) {
      ctx.json(pacemaker.getActivityReport(id)!!);
    } else {
      ctx.status(404)
    }
  }
	
	fun getFriendActivityReport(ctx: Context) {
    val id: String? =  ctx.param("id")
		val email: String? = ctx.param("email")
		val user = pacemaker.getUser(id!!)
    if (user != null) {
      ctx.json(pacemaker.getFriendActivityReport(id, email!!)!!);
    } else {
      ctx.status(404)
    }
  }
	
	
	
	
}
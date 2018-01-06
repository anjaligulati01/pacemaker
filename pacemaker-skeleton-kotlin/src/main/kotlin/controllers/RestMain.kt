package controllers

import io.javalin.Javalin

fun main(args: Array<String>) {
  val app = Javalin.create().port(getHerokuAssignedPort()).start()
  val service = PacemakerRestService()
  configRoutes(app, service)
}

private fun getHerokuAssignedPort(): Int {
  val processBuilder = ProcessBuilder()
  return if (processBuilder.environment()["PORT"] != null) {
    Integer.parseInt(processBuilder.environment()["PORT"])
  } else 7000
}

fun configRoutes(app: Javalin, service: PacemakerRestService) {
  app.get("/users") { ctx -> service.listUsers(ctx) }
  app.post("/users") { ctx -> service.createUser(ctx) }
  app.delete("/users") { ctx -> service.deleteUsers(ctx) }
	app.get("/users/:id/activities") { ctx -> service.getActivities(ctx) }
  app.get("/users/:id/activities/:activityId") { ctx -> service.getActivity(ctx) }
  app.post("/users/:id/activities") { ctx -> service.createActivity(ctx) }
  app.delete("/users/:id/activities") { ctx -> service.deleteActivites(ctx) }
	app.post("/users/:id/activities/:activityId/locations", { ctx -> 
      service.addLocation(ctx);
    });
	
	/*app.get("/users/:id/activities/:activityId/locations", { ctx -> 
      service.addLocation(ctx);
    });*/
	
	app.get("/activities/:activityId/locations", {ctx -> 
      service.getActivityLocations(ctx);
    });
	
	app.post("/users/:id/friend/:email", { ctx ->
      service.followFriend(ctx);
    });
	app.delete("/users/:id/friend/:email", { ctx ->
      service.unfollowFriend(ctx);
    });
	app.get("/users/:id/friends", { ctx ->
      service.listFriends(ctx);
    });
	
	app.get("/users/:id/activityReport", { ctx ->
      service.getActivityReport(ctx);
    });
	
	app.get("/users/:id/friends/:email/activities", { ctx ->
      service.getFriendActivityReport(ctx);
    });
}




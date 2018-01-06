package controllers;

import io.javalin.Javalin;

public class RestMain {

  public static void main(String[] args) throws Exception {
    //Javalin app = Javalin.start(7000);
    Javalin app = Javalin.create();
    
    app.port(getAssignedPort());
    app.start();
    PacemakerRestService service = new PacemakerRestService();
    configRoutes(app, service);
  }

  static void configRoutes(Javalin app, PacemakerRestService service) {

    app.get("/users", ctx -> {
      service.listUsers(ctx);
    });
    app.post("/users", ctx -> {
      service.createUser(ctx);
    });
    app.get("/users/:id", ctx -> {
      service.listUser(ctx);
    });
    app.get("/users/:id/activities", ctx -> {
      service.getActivities(ctx);
    });

    app.post("/users/:id/activities", ctx -> {
      service.createActivity(ctx);
    });
    app.get("/users/:id/activities/:activityId", ctx -> {
      service.getActivity(ctx);
    });
    
    app.get("/activities/:activityId/locations", ctx -> {
      service.getActivityLocations(ctx);
    });

    app.post("/users/:id/activities/:activityId/locations", ctx -> {
      service.addLocation(ctx);
    });
    
    app.delete("/users", ctx -> {
      service.deleteUsers(ctx);
    });

    app.delete("/users/:id", ctx -> {
      service.deletetUser(ctx);
    });
    
    app.delete("/users/:id/activities", ctx -> {
      service.deleteActivities(ctx);
    });
    ///users/:id/friends/message
    ///users/:id/friends/message
    app.post("/users/:id/friend/:email", ctx ->{
      service.follow(ctx);
    });
    
    app.get("/users/:id/friends", ctx ->{
      service.listFriends(ctx);
    });
    
    //FriendActivityReport
    app.get("/users/:id/friends/:email/activities", ctx ->{
      service.friendActivityReport(ctx);
    });
    
    //friendActivityReportBytype
    app.get("/users/:id/friends/:email/activities/:type", ctx ->{
      service.friendActivityReportByType(ctx);
    });
    
    app.delete("/users/:id/friends/:email/", ctx ->{
      service.unfollowFriend(ctx);
    });
    
    app.delete("/users/:id/friends", ctx ->{
      service.unfollowFriends(ctx);
    });
    
    app.get("/users/:id/activityReport", ctx ->{
      service.getActivityReport(ctx);
    });
    
    app.get("/users/:id/activityReport/:type", ctx ->{
      service.getActivityReportByType(ctx);
    });
    
    app.post("/users/:id/friend/:email/message", ctx ->{
      service.sendMessageToFriend(ctx);
    });
    
    app.get("/users/:id/messages", ctx ->{
      service.listMessages(ctx);
    });
    
    app.post("/users/:id/friends/message", ctx ->{
      service.sendMessageToAllFriends(ctx);
    });
    
    app.get("/users/:id/dlb", ctx ->{
      service.getDistanceLeaderBoard(ctx);
    });
    
    app.get("/users/:id/dlb/:type", ctx ->{
      service.getDistanceLeaderBoardByType(ctx);
    });
    
    app.get("/users/:id/llb/:loc", ctx ->{
      service.getLocationLeaderBoard(ctx);
    });
    
    app.post("/user/disable/:email", ctx ->{
      service.disableUser(ctx);
    });
    
    app.post("/user/enable/:email", ctx ->{
      service.enableUser(ctx);
    });
    
  }
  
  private static int getAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 7000;
  }
}
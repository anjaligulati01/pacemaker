package controllers;

import asg.cliche.Shell;
import asg.cliche.ShellFactory;

public class Main {

  public static void main(String[] args) throws Exception {
    PacemakerConsoleService main = new PacemakerConsoleService();
    Shell shell = ShellFactory
        .createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions", main);
    shell.commandLoop();
  }
}
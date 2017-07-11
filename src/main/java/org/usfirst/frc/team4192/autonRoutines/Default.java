package org.usfirst.frc.team4192.autonRoutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4192.commands.Move;

public class Default extends CommandGroup{
  public Default(){
    addSequential(new Move(90));
  }
  
}

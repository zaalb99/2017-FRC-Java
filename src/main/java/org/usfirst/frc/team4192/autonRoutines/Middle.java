package org.usfirst.frc.team4192.autonRoutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4192.commands.Actuate;
import org.usfirst.frc.team4192.commands.Move;

public class Middle extends CommandGroup{
  public Middle(){
    addSequential(new Move(74));
    addSequential(new Actuate());
  }
  
}

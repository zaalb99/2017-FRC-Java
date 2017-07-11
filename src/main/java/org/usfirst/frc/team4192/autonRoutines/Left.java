package org.usfirst.frc.team4192.autonRoutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4192.commands.Actuate;
import org.usfirst.frc.team4192.commands.Move;
import org.usfirst.frc.team4192.commands.Turn;

public class Left extends CommandGroup{
  public Left(){
    addSequential(new Move(53));
    addSequential(new Turn(60));
    addSequential(new Move(100));
    addSequential(new Actuate());
  }
  
}

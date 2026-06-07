import GUI.InputFrame;
import GUI.SimulationFrame;
import Model.*;
import BusinessLogic.*;
import java.io.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args)
    {
        try
        {
            com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme.setup();
        }
        catch( Exception ex )
        {
            System.err.println( "Failed to initialize the theme" );
        }
        new InputFrame();
    }
}
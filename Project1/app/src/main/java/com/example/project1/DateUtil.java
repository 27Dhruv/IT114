package com.example.project1;

import java.util.*;

////////////////////////////////////////////////////////////////
//
//  This module includes a utility method for accessing the
//  current year from the system.
//
//  USAGE: DateUtil.get_current_year()
//
//  Author: M. Halper
//
////////////////////////////////////////////////////////////////

public class DateUtil
{

    ////////////////////////////////////////////////////////////////

    public static int get_current_year()
    {
        //
        // This method returns the current year.  It uses Java's
        // built-in Calendar class.
        //

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());

        return cal.get(Calendar.YEAR);

    } // get_current_year

////////////////////////////////////////////////////////////////

} // end DateUtil
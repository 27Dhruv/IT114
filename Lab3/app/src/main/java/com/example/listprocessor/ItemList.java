package com.example.listprocessor;

import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;

//////////////////////////////////////////////////////////////////////////
//
//  Singleton class ItemList, for creating/managing a single
//  instance of a list of items (at the moment, strings).  Note
//  that the type "String" can easily be changed to accommodate a
//  list of whatever kind of objects you wish.
//
//  Author: M. Halper
//
//////////////////////////////////////////////////////////////////////////

@Singleton
public class ItemList extends LinkedList<String>
{
    @Inject
    ItemList()
    {
        // default constructor
    }

} // end ItemList

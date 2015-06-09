package com.pwdgame.widget;

import android.database.Cursor;
import android.widget.AlphabetIndexer;

public class XYAlphabeIndexer extends AlphabetIndexer{

	public XYAlphabeIndexer(Cursor cursor, int sortedColumnIndex,
			CharSequence alphabet) {
		super(cursor, sortedColumnIndex, alphabet);
	}

/*    *//**
     * Default implementation compares the first character of word with letter.
     *//*
	@Override
    protected int compare(String word, String letter) {
        final String firstLetter;
        if (word.length() == 0) {
            firstLetter = " ";
        } else {
            firstLetter = word.substring(0, 1);
        }
	
		if (firstLetter.matches("[A-Z]")&&letter.matches("[A-Z]")) {
			return firstLetter.compareTo(letter);
		}else if(firstLetter.equals("☆")&&firstLetter.equals(letter)){
			return 1;
		}else 
			return -1;
				
    }*/
}

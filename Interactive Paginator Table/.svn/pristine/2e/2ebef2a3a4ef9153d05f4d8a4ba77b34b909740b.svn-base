package com.ihsinformatics.interactivepaginatortable;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class PaginatorTableConfig implements Serializable {
	
	/**
	 * Generated serialVersionUID 
	 */
	private static final long serialVersionUID = 6130555288256304866L;
	
	private int numberOfRowsPerPage, pageTransformationStyle;
	private String[] columnHeadings;
	private ArrayList<String[]> data;
	private int fontSize = 18;
	private String title = "Title";
	private int stripColor1 = Color.WHITE;
	private int stripColor2 = Color.LTGRAY;
	private int fontColor = Color.BLACK;
	private int headingRowColor = Color.DKGRAY;
	private int headingFontColor = Color.WHITE;
	private int headingFontSize = 18;
	private int[] columnWeights;

	/*public PaginatorTableConfig() {
		
	}*/
	
	public PaginatorTableConfig(int numberOfRowsPerPage, String[] columnHeadings, ArrayList<String[]> data) throws ColumnsException {
		super();
		if(columnHeadings.length != data.get(0).length) {
			throw new ColumnsException("Number of columns for headings do not match to number of columns for data");
		}
		this.numberOfRowsPerPage = numberOfRowsPerPage;
		this.columnHeadings = columnHeadings;
		this.data = data;
	}

	public PaginatorTableConfig setNumberOfRowsPerPage(int numberOfRowsPerPage) {
		this.numberOfRowsPerPage = numberOfRowsPerPage;
		return this;
	}

	public PaginatorTableConfig setPageTransformationStyle(int pageTramsformationStyle) {
		this.pageTransformationStyle = pageTramsformationStyle;
		return this;

	}
	
	public PaginatorTableConfig setColumnsWeights(int... weights) throws ColumnsException {
		if(columnHeadings.length != weights.length) {
			throw new ColumnsException("Number of columns is less than number of column weights");
		}
		
		
		this.columnWeights = weights;
		return this;
	}
	
	public int[] getColumnsWeights() {
		return this.columnWeights;
	}
	
	public int getWeightSum() throws ColumnsException {
		if(columnWeights != null) {
			int weightSum = 0;
			int[] columnWeights = getColumnsWeights();
			for(int i=0; i<columnWeights.length; i++) {
				weightSum+=columnWeights[i];
			}
			
			return weightSum;
		}
		
		throw new ColumnsException("Column weights not set");
	}
	
	/*public PaginatorTableConfig setColumnHeadings(String... columnHeading) throws NumberOfColumnsException {
		if(columnHeading.length != data.get(0).length) {
			throw new NumberOfColumnsException("Number of columns for headings do not match to number of columns for data");
		}
		
		this.columnHeading = columnHeading;
		return this;
	}

	public PaginatorTableConfig setData(ArrayList<String[]> data) throws NumberOfColumnsException {
		if(columnHeading.length != data.get(0).length) {
			throw new NumberOfColumnsException("Number of columns for headings do not match to number of columns for data");
		}
		
		this.data = data;
		return this;
	}*/
	
	public PaginatorTableConfig setFontSize(int fontSize) {
		this.fontSize = fontSize;
		return this;
	}
	
	public PaginatorTableConfig setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public PaginatorTableConfig setStripColor1(int stripColor1) {
		this.stripColor1 = stripColor1;
		return this;
	}
	
	public PaginatorTableConfig setStripColor2(int stripColor2) {
		this.stripColor2 = stripColor2;
		return this;
	}
	
	public PaginatorTableConfig setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}
	
	public PaginatorTableConfig setHeadingRowColor(int headingRowColor) {
		this.headingRowColor = headingRowColor;
		return this;
	}
	
	public PaginatorTableConfig setHeadingFontColor(int headingFontColor) {
		this.headingFontColor = headingFontColor;
		return this;
	}
	
	public PaginatorTableConfig setHeadingFontSize(int headingFontSize) {
		this.headingFontSize = headingFontSize;
		return this;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getNumberOfRowsPerPage() {

		return this.numberOfRowsPerPage;
	}

	public int getPageTransformationStyle() {
		
		return this.pageTransformationStyle;
	}

	public String[] getColumnHeadings() {
		
		return this.columnHeadings;
	}

	public ArrayList<String[]> getData() {
		
		return this.data;
	}

	public int getFontSize() {
		
		return this.fontSize;
	}

	public String getTitle() {
		
		return this.title;
	}

	public int getStripColor1() {
		
		return this.stripColor1;
	}

	public int getStripColor2() {
		
		return this.stripColor2;
	}

	public int getFontColor() {
		
		return this.fontColor;
	}

	public int getHeadingRowColor() {
		
		return this.headingRowColor;
	}

	public int getHeadingFontColor() {
		
		return this.headingFontColor;
	}

	public int getHeadingFontSize() {
		
		return this.headingFontSize;
	}
	
	
	
	
	
}

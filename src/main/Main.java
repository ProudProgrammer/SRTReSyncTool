package main;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import core.SRT;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		SRT subtitle = SRT.createSRTFromFile("C:\\MyFolder\\MyTempFolder\\Area.51.2015.DVDRip.XviD-EVO.srt");
		SRT newSubtitle = SRT.createReSynchronizedSRT(subtitle, 60000);
		newSubtitle.setName("Test.srt");
		SRT.save(newSubtitle, "C:\\MyFolder\\MyTempFolder");
	}
}

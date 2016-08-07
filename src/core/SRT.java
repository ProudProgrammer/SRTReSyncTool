package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SRT {

	private static final String DEFAULT_SRT_TRACK_LINE_REGEXP = "\\d{2}:\\d{2}:\\d{2},\\d{3}\\s-->\\s\\d{2}:\\d{2}:\\d{2},\\d{3}";
	private static final String DEFAULT_NAME = "SRT.srt";
	private static final Charset DEFAULT_CHARSET = Charset.forName("windows-1250");
	
	private static final NumberFormat twoDigitNumberFormat = NumberFormat.getNumberInstance();
	private static final NumberFormat threeDigitNumberFormat = NumberFormat.getNumberInstance();
	
	private String srt;
	private String name;
	private Charset charset;
	
	static {
		twoDigitNumberFormat.setMinimumIntegerDigits(2);
		threeDigitNumberFormat.setMinimumIntegerDigits(3);
	}
	
	public SRT(CharSequence srt, String name, Charset charset) {
		this.srt = srt.toString();
		this.name = name;
		this.charset = charset;
	}
	
	public SRT(CharSequence srt, String name) {
		this(srt, name, DEFAULT_CHARSET);
	}
	
	public SRT(CharSequence srt, Charset charset) {
		this(srt, DEFAULT_NAME, charset);
	}
	
	public SRT(CharSequence srt) {
		this(srt, DEFAULT_NAME, DEFAULT_CHARSET);
	}
	
	public static boolean isValidSRTTrackLine(CharSequence trackLine) {
		return Pattern.matches(DEFAULT_SRT_TRACK_LINE_REGEXP, trackLine);
	}
	
	public boolean isEmptySRT() {
		return (srt.equals("")) ? true : false;
	}
	
	public static long trackToMilliseconds(String track) {
		long hours = Long.parseLong(track.substring(0, 2));
		long minutes = Long.parseLong(track.substring(3, 5));
		long seconds = Long.parseLong(track.substring(6, 8));
		long milliseconds = Long.parseLong(track.substring(9, 12));
		return milliseconds + seconds*1000 + minutes*60*1000 + hours*60*60*1000; 
	}
	
	public static String millisecondsToTrack(long milliseconds) {
		if(milliseconds == 0) {
			return "00:00:00,000";
		}
		long hours = milliseconds/(60*60*1000);
		milliseconds -= hours*60*60*1000;
		long minutes = milliseconds/(60*1000);
		milliseconds -= minutes*60*1000;
		long seconds = milliseconds/1000;
		milliseconds -= seconds*1000;
		return twoDigitNumberFormat.format(hours) + ":" + twoDigitNumberFormat.format(minutes) + ":" + twoDigitNumberFormat.format(seconds) + "," + threeDigitNumberFormat.format(milliseconds);
	}
	
	public static String shift(String track, long offset) {
		return millisecondsToTrack(trackToMilliseconds(track) + offset);
	}
	
	public static long getDifferenceBetweenTwoTracksInMilliseconds(long minuend, long subtrahend) {
		return minuend - subtrahend;
	}
	
	public static long getDifferenceBetweenTwoTracksInMilliseconds(String minuendTrack, String subtrahendTrack) {
		return trackToMilliseconds(minuendTrack) - trackToMilliseconds(subtrahendTrack);
	}
	
	public static SRT createReSynchronizedSRT(SRT srt, long offset) {
		if(srt.isEmptySRT() || offset == 0) {
			return srt;
		}
		StringBuilder synchronizedSRTBuilder = new StringBuilder();
		Scanner srtScanner = new Scanner(srt.toString());
		String srtLine = "";
		String[] srtTracks = null;
		boolean firstLine = true;
		while(srtScanner.hasNext()) {
			if(!firstLine) {
				synchronizedSRTBuilder.append("\n");
			} else {
				firstLine = false;
			}
			srtLine = srtScanner.nextLine();
			if(isValidSRTTrackLine(srtLine)) {
				srtTracks = srtLine.split(" --> ");
				synchronizedSRTBuilder.append(shift(srtTracks[0], offset) + " --> " + shift(srtTracks[1], offset));
			} else {
				synchronizedSRTBuilder.append(srtLine);
			}
		}
		srtScanner.close();
		return new SRT(synchronizedSRTBuilder, srt.name, srt.charset);
	}
	
	public static SRT createSRTFromFile(File file, Charset charset) throws FileNotFoundException {
		StringBuilder srtBuilder = new StringBuilder();
		Scanner fileScanner = new Scanner(file, charset.name());
		while(fileScanner.hasNext()) {
			srtBuilder.append("\n");
			srtBuilder.append(fileScanner.nextLine());
		}
		fileScanner.close();
		srtBuilder.delete(0, 1);
		return new SRT(srtBuilder.toString(), file.getName(), charset);
	}
	
	public static SRT createSRTFromFile(File file) throws FileNotFoundException {
		return createSRTFromFile(file, DEFAULT_CHARSET);
	}
	
	public static SRT createSRTFromFile(String path, Charset charset) throws FileNotFoundException {
		return createSRTFromFile(new File(path), charset);
	}
	
	public static SRT createSRTFromFile(String path) throws FileNotFoundException {
		return createSRTFromFile(new File(path), DEFAULT_CHARSET);
	}
	
	public static void save(SRT srt, File path) throws FileNotFoundException, UnsupportedEncodingException {
		File newSRTFile = null;
		if(path.isFile()) {
			newSRTFile = new File(path.getAbsolutePath());
		} else {
			path.mkdirs();
			newSRTFile = new File(path.getAbsolutePath() + File.separator + srt.name);
		}
		Scanner srtScanner = new Scanner(srt.toString());
		PrintWriter printWriter = new PrintWriter(newSRTFile, srt.charset.name());
		boolean firstLine = true;
		while(srtScanner.hasNext()) {
			if(!firstLine) {
				printWriter.print("\n");
			} else {
				firstLine = false;
			}
			printWriter.print(srtScanner.nextLine());
		}
		srtScanner.close();
		printWriter.close();
	}
	
	public static void save(SRT srt, String path) throws FileNotFoundException, UnsupportedEncodingException {
		save(srt, new File(path));
	}
	
	public String toString() {
		return this.srt;
	}

	public String getSRT() {
		return srt;
	}

	public void setSRT(String srt) {
		this.srt = srt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
}

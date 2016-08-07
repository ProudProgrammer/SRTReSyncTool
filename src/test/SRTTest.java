package test;

import static org.junit.Assert.*;

import org.junit.Test;

import core.SRT;

public class SRTTest {

	@Test
	public void testIsValidSRTTrackLine() {
		String trackline = "00:01:59,456 --> 00:02:02,129";
		assertTrue(SRT.isValidSRTTrackLine(trackline));
	}
	
	@Test
	public void testIsEmptySRT() {
		assertTrue(new SRT("").isEmptySRT());
	}
	
	@Test
	public void testTrackToMilliseconds() {
		String track = "11:38:35,899";
		long expected = 41915899L;
		assertEquals(expected, SRT.trackToMilliseconds(track));
	}
	
	@Test
	public void testMillisecondsToTrack() {
		long milliseconds = 60000;
		String expected = "00:01:00,000";
		assertEquals(expected, SRT.millisecondsToTrack(milliseconds));
	}
	
	@Test
	public void testShift() {
		String track = "01:15:30,200";
		long offset = 300300L;
		String expected = "01:20:30,500";
		assertEquals(expected, SRT.shift(track, offset));
	}
	
	@Test
	public void testGetDifferenceBetweenTwoTracksInMilliseconds() {
		assertEquals(600000, SRT.getDifferenceBetweenTwoTracksInMilliseconds(41915899, 41315899));
	}
	
	@Test
	public void testGetDifferenceBetweenTwoTracksInMilliseconds2() {
		assertEquals(600000, SRT.getDifferenceBetweenTwoTracksInMilliseconds("11:38:35,899", "11:28:35,899"));
	}
	
}

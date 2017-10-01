package ru.spbau.sazanovich.nikita;

import org.junit.Test;

import static org.junit.Assert.*;

public class Task3BTest {

  @Test
  public void testAccept1() throws Exception {
    assertTrue(Task3B.getAutomaton().run("()"));
  }

  @Test
  public void testAccept2() throws Exception {
    assertTrue(Task3B.getAutomaton().run("(1)"));
  }

  @Test
  public void testAccept3() throws Exception {
    assertTrue(Task3B.getAutomaton().run("(1,1,2,3,5,8)"));
  }

  @Test
  public void testAccept4() throws Exception {
    assertTrue(Task3B.getAutomaton().run("(_)"));
  }

  @Test
  public void testAccept5() throws Exception {
    assertTrue(Task3B.getAutomaton().run("(_4,_eight,_15,_[13;_16_;_23;_42])"));
  }

  @Test
  public void testReject1() throws Exception {
    assertFalse(Task3B.getAutomaton().run(")("));
  }

  @Test
  public void testReject2() throws Exception {
    assertFalse(Task3B.getAutomaton().run("(())"));
  }

  @Test
  public void testReject3() throws Exception {
    assertFalse(Task3B.getAutomaton().run("(1,2,3"));
  }

  @Test
  public void testReject4() throws Exception {
    assertFalse(Task3B.getAutomaton().run("(1;2;3)"));
  }

  @Test
  public void testReject5() throws Exception {
    assertFalse(Task3B.getAutomaton().run("(1,2,)"));
  }

  @Test
  public void testReject6() throws Exception {
    assertFalse(Task3B.getAutomaton().run("(1, 23 4, 5)"));
  }
}

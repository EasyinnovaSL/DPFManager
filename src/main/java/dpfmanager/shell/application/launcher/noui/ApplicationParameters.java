package dpfmanager.shell.application.launcher.noui;

/**
 * Created by Adrià Llorens on 18/04/2016.
 */
public class ApplicationParameters {

  private boolean silence;
  private int recursive;

  public ApplicationParameters(){
    silence = false;
    recursive = 1;
  }

  public boolean isSilence() {
    return silence;
  }

  public void setSilence(boolean silence) {
    this.silence = silence;
  }

  public int getRecursive() {
    return recursive;
  }

  public void setRecursive(int recursive) {
    this.recursive = recursive;
  }
}

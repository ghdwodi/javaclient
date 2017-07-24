package quiz;

import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class AddStyles {
	public void addStyles(StyledDocument sdoc){
		Style style = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		Style s = sdoc.addStyle("BLACKnBOLD", style);
		StyleConstants.setForeground(s, Color.BLACK);
		StyleConstants.setBold(s, true);
		
		s = sdoc.addStyle("BLACK", style);
		StyleConstants.setForeground(s, Color.BLACK);
		
		s = sdoc.addStyle("RED", style);
		StyleConstants.setForeground(s, Color.RED);
		
		s = sdoc.addStyle("GREEN", style);
		StyleConstants.setForeground(s, Color.GREEN);
		
		s = sdoc.addStyle("BLUE", style);
		StyleConstants.setForeground(s, Color.BLUE);
		
		s = sdoc.addStyle("CYAN", style);
		StyleConstants.setForeground(s, Color.CYAN);
		
		s = sdoc.addStyle("MAGENTA", style);
		StyleConstants.setForeground(s, Color.MAGENTA);
		
		s = sdoc.addStyle("YELLOW", style);
		StyleConstants.setForeground(s, Color.YELLOW);
	}
}

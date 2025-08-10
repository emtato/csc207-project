package view;

import java.awt.Color;
import java.awt.Font;

public class GUIConstants {
    // GENERAL (MULTIPLE PAGES)
    public static final int WINDOW_WIDTH = 1440;
    public static int WINDOW_HEIGHT = 900;
    
    // STANDARDIZED PAGE DIMENSIONS
    public static final int STANDARD_PAGE_WIDTH = 1440;
    public static final int STANDARD_PAGE_HEIGHT = 900;
    public static final int STANDARD_SCROLL_WIDTH = 1200;
    public static final int STANDARD_SCROLL_HEIGHT = 600;
    
    // STANDARDIZED COMPONENT SIZES
    public static final int STANDARD_PANEL_WIDTH = 500;
    public static final int STANDARD_PANEL_HEIGHT = 400;
    public static final int STANDARD_BUTTON_WIDTH = 250;
    public static final int STANDARD_BUTTON_HEIGHT = 75;
    public static final int STANDARD_ICON_SIZE = 150;
    public static final int STANDARD_SMALL_ICON_SIZE = 100;
    
    // EXISTING CONSTANTS
    public static final int BUTTON_WIDTH = 268;
    public static final int BUTTON_HEIGHT = 73;

    public static final Color RED = new Color(250,59,78); // Hex Code: FA3B4E
    public static final Color PINK = new Color(255,204,203);
    public static final Color WHITE = Color.WHITE;
    public static final Color BLACK = new Color(25,10,10);

    public static final int TITLE_SIZE = 40;
    public static final int HEADER_SIZE = 35;
    public static final int TEXT_SIZE = 25;
    public static final int SMALL_TEXT_SIZE = 18;

    public static final String FONT = "Inter";
    public static final Font FONT_TEXT = new Font(FONT, Font.PLAIN, TEXT_SIZE);
    public static final Font SMALL_FONT_TEXT = new Font(FONT, Font.PLAIN, SMALL_TEXT_SIZE);
    public static final Font FONT_HEADER = new Font(FONT, Font.PLAIN, HEADER_SIZE);
    public static final Font FONT_TITLE = new Font(FONT, Font.BOLD, TITLE_SIZE);

    public static final int COMPONENT_GAP_SIZE = 20;

    // MAIN PAGE
    public static final int MAIN_BUTTON1_WIDTH = 250;
    public static final int MAIN_BUTTON1_HEIGHT = 75;
    public static final int MAIN_BUTTON2_SIDE = 125;
    public static final int FEED_WIDTH = 1050;
    public static final int FEED_HEIGHT = 925;
    public static final Font FONT_SIDE_MENU = new Font(FONT, Font.PLAIN, 48);

    // FOLLOWING AND FOLLOWERS PAGES
    public static final int PROFILE_PICTURE_WIDTH = 200;
    public static final int PROFILE_PICTURE_HEIGHT = 200;
}

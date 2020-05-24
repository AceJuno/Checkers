
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static java.lang.Math.floor;

public class Graphics extends Canvas {

        //Definition of colors to use in drawing.
        private final Color blackColor = new Color (0,0,0, 1);
        private final Color whiteColor = new Color (1,1,1, 1);
        private final Color yellowHighlight = new Color (1,1,0, 0.9);
        private final Color greenHighlight = new Color (0,1,0, 0.9);
        private final Color orangeHighlight = new Color (1,0.3,0, 0.9);
        private final Color backboard = new Color(0.2,0.05,0,0.95 );

        private final RadialGradient whitePawnGradient = RadialGradient.valueOf("center 50% 50% , radius 51% , repeat, #0000ff 30%, #000000 60%, #0000ff, #000000");
        private final RadialGradient blackPawnGradient = RadialGradient.valueOf("center 50% 50% , radius 51% , repeat, #ff0000 30%, #000000 60%, #ff0000, #000000");

        private final RadialGradient whiteQueenGradient = RadialGradient.valueOf("center 50% 50% , radius 20% , repeat, #000000 5%, #0023ff 50%, #000000");
        private final RadialGradient blackQueenGradient = RadialGradient.valueOf("center 50% 50% , radius 20% , repeat, #000000 5%, #ff0000 50%, #000000");

        //Declaration of GameEngine used to run game.
        private final GameEngine eng = new GameEngine();
        //Declaration of GraphicsContext to display game.
        private final GraphicsContext gc = getGraphicsContext2D();
        private final Label turn = new Label("White turn");

        public Graphics ()
        {
            widthProperty().addListener(evt -> drawBoard(gc));
            heightProperty().addListener(evt -> drawBoard(gc));
            setOnMouseClicked(evt ->
                    {
                     double x = ( 800 - getWidth()) / 2 + evt.getX();
                     double y = ( 800 - getHeight()) / 2 + evt.getY();
                     int col = (int) floor((( 800 - getWidth()) / 2 + evt.getX()) / 100);
                     int row = (int) floor((( 800 - getHeight()) / 2 + evt.getY()) / 100);
                     if(row > 7 || row < 0 || col > 7 || col < 0) return;

                     eng.turn(row, col);
                     drawBoard(gc);
                    });
        }

        private void drawBoard(GraphicsContext gc) {
            gc.clearRect(0, 0, this.getWidth(), this.getHeight());
            gc.setFill(backboard);
            gc.fillRoundRect((getWidth() - 850) / 2, (getHeight() - 850) / 2 , 850 ,850, 40,40);
            gc.setFill(blackColor);
            for (int i = 0; i < 8; i++)
            {
                for (int k = 0; k < 8; k++)
                {
                    if(i % 2 == 0) {
                        if (k % 2 == 1) gc.setFill(blackColor);
                        else gc.setFill(whiteColor);
                    }
                    else
                    {
                        if (k % 2 == 0) gc.setFill(blackColor);
                        else gc.setFill(whiteColor);
                    }
                    gc.fillRect((800 * i / 8) + ((getWidth() - 800) / 2), (800 * k / 8) + ((getHeight() - 800) / 2) , 800 / 8,800 / 8);

                }
            }
            gc.setFont(Font.font ("Gruppo", 35));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            if (eng.whiteTurn)gc.fillText("Blue turn",(getWidth())/2, (getHeight() - 880) / 2 );
            else gc.fillText("Red turn",(getWidth())/2, (getHeight() - 880) / 2 );

            drawAttacks();
            drawMoves();
            highlightPawn();
            drawBlackPawns();
            drawWhitePawns();
        }

        @Override
        public boolean isResizable() {
        return true;
    }

        @Override
        public double prefWidth(double height) {
        return getWidth();
    }

        @Override
        public double prefHeight(double width) {
        return getHeight();
    }

    public void highlightPawn()
    {
        gc.setFill(yellowHighlight);
        gc.clearRect((100 * eng.highlightedPawn.col) + ((getWidth() - 800) / 2) , (100 * eng.highlightedPawn.row ) + ((getHeight() - 800) / 2)  , 800 / 8,800 / 8);
        gc.fillRect((100 * eng.highlightedPawn.col) + ((getWidth() - 800) / 2) -1, (100 * eng.highlightedPawn.row)  + ((getHeight() - 800) / 2)-1 , 101.5,101.5);
    }

    public void drawBlackPawns()
    {
        for (Pawn p1 : eng.blackPawns)
        {
            if(p1.isQueen)
            {
                gc.setFill(blackQueenGradient);
            }
            else
            {
                gc.setFill(blackPawnGradient);
            }
            gc.fillOval((800 * p1.col / 8) + ((getWidth() - 800) / 2) + 5, (800 * p1.row / 8) + ((getHeight() - 800) / 2) + 5, 90,90);
        }
    }

    public void drawWhitePawns()
    {
        for (Pawn p1 : eng.whitePawns)
        {

            if(p1.isQueen)
            {
                gc.setFill(whiteQueenGradient);
            }
            else
            {
                gc.setFill(whitePawnGradient);
            }
            gc.fillOval((800 * p1.col / 8) + ((getWidth() - 800) / 2) + 5, (800 * p1.row / 8) + ((getHeight() - 800) / 2) + 5, 90,90);
        }
    }

    public void drawMoves()
    {
        {
            gc.setFill(greenHighlight);
            for(Point point : eng.posMoves)
            {
                gc.clearRect((100 * point.col) + ((getWidth() - 800) / 2) , (100 * point.row ) + ((getHeight() - 800) / 2)  , 800 / 8,800 / 8);
                gc.fillRect((100 * point.col) + ((getWidth() - 800) / 2) -1, (100 * point.row )  + ((getHeight() - 800) / 2)-1 , 101,101);
            }
        }
    }

    public void drawAttacks()
    {
        gc.setFill(orangeHighlight);
        for(Point point : eng.posAttacks)
        {
            gc.clearRect((100 * point.col) + ((getWidth() - 800) / 2) , (100 * point.row ) + ((getHeight() - 800) / 2)  , 800 / 8,800 / 8);
            gc.fillRect((100 * point.col) + ((getWidth() - 800) / 2) -1, (100 * point.row )  + ((getHeight() - 800) / 2)-1 , 101,101);
        }
    }



}

package GameView.Map;

import GameModel.Map.Tile.VolcanoTile;
import GameView.ImagePaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Created by jowens on 3/23/17.
 */
public class VolcanoView extends HexTileView{
    public VolcanoView(VolcanoTile vt){
        super(vt);
        Random random = new Random();
        try {
            switch(random.nextInt(ImagePaths.NUMBER_VOLCANOS)) {
                case 0:
                    myImage = ImageIO.read(new File(ImagePaths.VOLCANO_TERRAIN));
            }
        }
        catch (IOException e) {
            System.out.println("Volcano File not found");
        }
    }

}

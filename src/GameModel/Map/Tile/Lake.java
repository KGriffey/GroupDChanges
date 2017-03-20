package GameModel.Map.Tile;

/**
 * Created by jowens on 3/13/17.
 */
public class Lake extends TerrainTile{
    @Override
    public boolean isLake(){
        return true;
    }
    @Override
    public String toString(){
        return "Lake";
    }

    @Override
    public boolean ofSameType(HexTile ht){
        return ht.ofSameType(this);
    }

    public boolean ofSameType(Grass gt){
        return false;
    }
    public boolean ofSameType(Rock gt){
        return false;
    }
    public boolean ofSameType(Lake gt){
        return true;
    }
    public boolean ofSameType(Jungle gt){
        return false;
    }
    public boolean ofSameType(VolcanoTile volcanoTile){
        return false;
    }
}

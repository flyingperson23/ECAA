package flyingperson.ecaa.block.gravity.t3;

import flyingperson.ecaa.block.gravity.BlockGravity;
import flyingperson.ecaa.block.gravity.TileEntityGravity;

public class GravityBlockt3 extends BlockGravity {
    public GravityBlockt3() {
        super("gravityt3");
    }

    @Override
    public TileEntityGravity createNewTileEntity() {
        return new GravityTEt3();
    }
}

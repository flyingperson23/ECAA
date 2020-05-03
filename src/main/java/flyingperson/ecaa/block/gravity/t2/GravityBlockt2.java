package flyingperson.ecaa.block.gravity.t2;

import flyingperson.ecaa.block.gravity.BlockGravity;
import flyingperson.ecaa.block.gravity.TileEntityGravity;

public class GravityBlockt2 extends BlockGravity {
    public GravityBlockt2() {
        super("gravityt2");
    }

    @Override
    public TileEntityGravity createNewTileEntity() {
        return new GravityTEt2();
    }
}

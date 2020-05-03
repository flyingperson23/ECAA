package flyingperson.ecaa.block.gravity.t1;

import flyingperson.ecaa.block.gravity.BlockGravity;
import flyingperson.ecaa.block.gravity.TileEntityGravity;

public class GravityBlockt1 extends BlockGravity {
    public GravityBlockt1() {
        super("gravityt1");
    }

    @Override
    public TileEntityGravity createNewTileEntity() {
        return new GravityTEt1();
    }
}

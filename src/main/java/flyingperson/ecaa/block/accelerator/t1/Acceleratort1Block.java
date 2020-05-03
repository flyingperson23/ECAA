package flyingperson.ecaa.block.accelerator.t1;

import flyingperson.ecaa.block.accelerator.AcceleratorBlock;
import flyingperson.ecaa.block.accelerator.AcceleratorTileEntity;

public class Acceleratort1Block extends AcceleratorBlock {

    public Acceleratort1Block() {
        super("acceleratort1");
    }

    @Override
    public AcceleratorTileEntity createNewTileEntity() {
        return new flyingperson.ecaa.block.accelerator.t1.Acceleratort1TileEntity();
    }
}

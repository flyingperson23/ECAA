package flyingperson.ecaa.block.accelerator.t3;

import flyingperson.ecaa.block.accelerator.AcceleratorBlock;
import flyingperson.ecaa.block.accelerator.AcceleratorTileEntity;
import net.minecraft.tileentity.TileEntity;

public class Acceleratort3Block extends AcceleratorBlock {

    public Acceleratort3Block() {
        super("acceleratort3");
    }

    @Override
    public AcceleratorTileEntity createNewTileEntity() {
        return new Acceleratort3TileEntity();
    }
}

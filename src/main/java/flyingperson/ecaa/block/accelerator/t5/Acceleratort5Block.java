package flyingperson.ecaa.block.accelerator.t5;

import flyingperson.ecaa.block.accelerator.AcceleratorBlock;
import flyingperson.ecaa.block.accelerator.AcceleratorTileEntity;
import net.minecraft.tileentity.TileEntity;

public class Acceleratort5Block extends AcceleratorBlock {

    public Acceleratort5Block() {
        super("acceleratort5");
    }

    @Override
    public AcceleratorTileEntity createNewTileEntity() {
        return new Acceleratort5TileEntity();
    }
}

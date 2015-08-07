package redstonedude.plugins.siege.entity;

import redstonedude.plugins.siege.handler.PlayerEntityHandlerListener;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.DifficultyDamageScaler;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.GroupDataEntity;
import net.minecraft.server.v1_8_R3.IMonster;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldType;

public class EntityBlankSlime extends EntityInsentient
implements IMonster {
    public float a;
    public float b;
    public float c;
    private boolean bk;

    public EntityBlankSlime(World world) {
        super(world);
        this.datawatcher.watch(16, (Object)Byte.valueOf((byte)2));
        this.setSize(0.51000005f * (float)0, 0.51000005f * (float)0);
        this.b_ = 0;
        this.width = 0F;
        this.length = 0;
        this.noclip = true;
        AxisAlignedBB axis = new AxisAlignedBB(0,0,0,0,0,0);
        this.a(axis);
        PlayerEntityHandlerListener.ideas.add(this.getBukkitEntity().getEntityId());
    }
    
    protected void h() {
        super.h();
        this.datawatcher.a(16, (Object)Byte.valueOf((byte) 1));
    }

    public void setSize(int i) {
        this.datawatcher.watch(16, (Object)Byte.valueOf((byte)i));
        this.setSize(0.51000005f * (float)i, 0.51000005f * (float)i);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double)(i * i));
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue((double)(0.2f + 0.1f * (float)i));
        this.setHealth(this.getMaxHealth());
        this.b_ = i;
    }

    public int getSize() {
        return this.datawatcher.getByte(16);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Size", this.getSize() - 1);
        nbttagcompound.setBoolean("wasOnGround", this.bk);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        int i = nbttagcompound.getInt("Size");
        if (i < 0) {
            i = 0;
        }
        this.setSize(i + 1);
        this.bk = nbttagcompound.getBoolean("wasOnGround");
    }

    protected EnumParticle n() {
        return EnumParticle.SLIME;
    }

    protected void ch() {
        this.a*=0.6f;
    }

    protected int cg() {
        return this.random.nextInt(20) + 10;
    }

    protected EntitySlime cf() {
        return new EntitySlime(this.world);
    }

    public void i(int i) {
        if (i == 16) {
            int j = this.getSize();
            this.setSize(0.51000005f * (float)j, 0.51000005f * (float)j);
            this.yaw = this.aK;
            this.aI = this.aK;
            if (this.V() && this.random.nextInt(20) == 0) {
                this.X();
            }
        }
        super.i(i);
    }

    public void die() {
    	//this.getBukkitEntity().getPassenger().remove();
        //super.die();
    }

    public void collide(Entity entity) {
        super.collide(entity);
        if (entity instanceof EntityIronGolem && this.ci()) {
            this.e((EntityLiving)entity);
        }
    }

    public void d(EntityHuman entityhuman) {
        if (this.ci()) {
            this.e((EntityLiving)entityhuman);
        }
    }

    protected void e(EntityLiving entityliving) {
        int i = this.getSize();
        if (this.hasLineOfSight((Entity)entityliving) && this.h((Entity)entityliving) < 0.6 * (double)i * 0.6 * (double)i && entityliving.damageEntity(DamageSource.mobAttack((EntityLiving)this), (float)this.cj())) {
            this.makeSound("mob.attack", 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            this.a((EntityLiving)this, (Entity)entityliving);
        }
    }

    public float getHeadHeight() {
        return 0.625f * this.length;
    }

    protected boolean ci() {
        if (this.getSize() > 1) {
            return true;
        }
        return false;
    }

    protected int cj() {
        return this.getSize();
    }

    protected String bo() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    protected String bp() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    protected Item getLoot() {
        return this.getSize() == 1 ? Items.SLIME : null;
    }

    public boolean bR() {
        BlockPosition blockposition = new BlockPosition(MathHelper.floor((double)this.locX), 0, MathHelper.floor((double)this.locZ));
        Chunk chunk = this.world.getChunkAtWorldCoords(blockposition);
        if (this.world.getWorldData().getType() == WorldType.FLAT && this.random.nextInt(4) != 1) {
            return false;
        }
        if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            BiomeBase biomebase = this.world.getBiome(blockposition);
            if (biomebase == BiomeBase.SWAMPLAND && this.locY > 50.0 && this.locY < 70.0 && this.random.nextFloat() < 0.5f && this.random.nextFloat() < this.world.y() && this.world.getLightLevel(new BlockPosition((Entity)this)) <= this.random.nextInt(8)) {
                return super.bR();
            }
            if (this.random.nextInt(10) == 0 && chunk.a(987234911).nextInt(10) == 0 && this.locY < 40.0) {
                return super.bR();
            }
        }
        return false;
    }

    protected float bB() {
        return 0.4f * (float)this.getSize();
    }

    public int bQ() {
        return 0;
    }

    protected boolean cn() {
        if (this.getSize() > 0) {
            return true;
        }
        return false;
    }

    protected boolean cl() {
        if (this.getSize() > 2) {
            return true;
        }
        return false;
    }

    protected void bF() {
        this.motY = 0.41999998688697815;
        this.ai = true;
    }

    public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, GroupDataEntity groupdataentity) {
        int i = this.random.nextInt(3);
        if (i < 2 && this.random.nextFloat() < 0.5f * difficultydamagescaler.c()) {
            ++i;
        }
        int j = 1 << i;
        this.setSize(j);
        return super.prepare(difficultydamagescaler, groupdataentity);
    }
    
}
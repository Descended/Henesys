package henesys.connection.packet;

import henesys.connection.OutPacket;
import henesys.handlers.header.OutHeader;
import henesys.life.npc.Npc;

public class NpcPool {

	public static OutPacket npcEnterField(Npc npc) {
		OutPacket outPacket = new OutPacket(OutHeader.NPC_ENTER_FIELD);

		outPacket.encodeInt(npc.getObjectId());
		outPacket.encodeInt(npc.getTemplateId());
		npc.encode(outPacket);

		return outPacket;
	}

	public static OutPacket npcLeaveField(Npc npc) {
		OutPacket outPacket = new OutPacket(OutHeader.NPC_LEAVE_FIELD);

		outPacket.encodeInt(npc.getObjectId());

		return outPacket;
	}

	public static OutPacket npcChangeController(Npc npc, boolean controller, boolean remove) {
		OutPacket outPacket = new OutPacket(OutHeader.NPC_CHANGE_CONTROLLER);

		outPacket.encodeByte(controller);
		outPacket.encodeInt(npc.getObjectId());
		if (!remove) {
			outPacket.encodeInt(npc.getTemplateId());
			npc.encode(outPacket);
		}

		return outPacket;
	}
}

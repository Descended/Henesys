package henesys.constants;

import henesys.items.BodyPart;
import henesys.items.EquipPrefix;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;

import static henesys.items.BodyPart.*;

public class ItemConstants {

    static final org.apache.logging.log4j.Logger log = LogManager.getRootLogger();
    public static int getBodyPartFromItem(int nItemID, int gender) {
        List<Integer> arr = getBodyPartArrayFromItem(nItemID, gender);
        return (!arr.isEmpty()) ? arr.getFirst() : 0;
    }

    public static List<Integer> getBodyPartArrayFromItem(int itemID, int genderArg) {
        int gender = getGenderFromId(itemID);
        EquipPrefix prefix = EquipPrefix.getByVal(getItemPrefix(itemID));
        List<Integer> bodyPartList = new ArrayList<>();
        if (gender != 2 && genderArg != 2 && gender != genderArg) {
            return bodyPartList;
        }
        if (prefix != null) {
            switch (prefix) {
                case Hat:
                    bodyPartList.add(Hat.getVal());
                    bodyPartList.add(BodyPart.EvanHat.getVal());
                    break;
                case FaceAccessory:
                    bodyPartList.add(FaceAccessory.getVal());
                    break;
                case EyeAccessory:
                    bodyPartList.add(EyeAccessory.getVal());
                    break;
                case Earrings:
                    bodyPartList.add(BodyPart.Earrings.getVal());
                    break;
                case Top:
                case Overall:
                    bodyPartList.add(BodyPart.Top.getVal());
                    break;
                case Bottom:
                    bodyPartList.add(BodyPart.Bottom.getVal());
                    break;
                case Shoes:
                    bodyPartList.add(BodyPart.Shoes.getVal());
                    break;
                case Gloves:
                    bodyPartList.add(BodyPart.Gloves.getVal());
                    break;
                case Shield:
                case Katara:
                case SecondaryWeapon:
                    bodyPartList.add(BodyPart.Shield.getVal());
                    break;
                case Cape:
                    bodyPartList.add(BodyPart.Cape.getVal());
                    break;
                case Ring:
                    bodyPartList.add(BodyPart.Ring1.getVal());
                    bodyPartList.add(BodyPart.Ring2.getVal());
                    bodyPartList.add(BodyPart.Ring3.getVal());
                    bodyPartList.add(BodyPart.Ring4.getVal());
                    break;
                case Pendant:
                    bodyPartList.add(BodyPart.Pendant.getVal());
                    break;
                case Belt:
                    bodyPartList.add(BodyPart.Belt.getVal());
                    break;
                case Medal:
                    bodyPartList.add(BodyPart.Medal.getVal());
                    break;
                case Shoulder:
                    bodyPartList.add(BodyPart.Shoulder.getVal());
                    break;
                case MachineEngine:
                    bodyPartList.add(BodyPart.MachineEngine.getVal());
                    break;
                case MachineArm:
                    bodyPartList.add(BodyPart.MachineArm.getVal());
                    break;
                case MachineLeg:
                    bodyPartList.add(BodyPart.MachineLeg.getVal());
                    break;
                case MachineFrame:
                    bodyPartList.add(BodyPart.MachineFrame.getVal());
                    break;
                case MachineTransistor:
                    bodyPartList.add(BodyPart.MachineTransistor.getVal());
                    break;
                case PetWear:
                    bodyPartList.add(BodyPart.PetWear1.getVal());
                    bodyPartList.add(BodyPart.PetWear2.getVal());
                    bodyPartList.add(BodyPart.PetWear3.getVal());
                    break;
                // case 184: // unknown, equip names are untranslated and google search results in hekaton screenshots
                // case 185:
                // case 186:
                // case 187:
                // case 188:
                // case 189:
                case TamingMob:
                    bodyPartList.add(BodyPart.TamingMob.getVal());
                    break;
                case Saddle:
                    bodyPartList.add(BodyPart.Saddle.getVal());
                    break;
                case EvanHat:
                    bodyPartList.add(BodyPart.EvanHat.getVal());
                    break;
                case EvanPendant:
                    bodyPartList.add(BodyPart.EvanPendant.getVal());
                    break;
                case EvanWing:
                    bodyPartList.add(BodyPart.EvanWing.getVal());
                    break;
                case EvanShoes:
                    bodyPartList.add(BodyPart.EvanShoes.getVal());
                    break;
                default:
                    if (ItemConstants.isWeapon(itemID)) {
                        bodyPartList.add(BodyPart.Weapon.getVal());
                    } else {
                        log.debug("Unknown type? id = {}", itemID);
                    }
                    break;
            }
        } else {
            log.debug("Unknown type? id = {}", itemID);
        }
        return bodyPartList;

    }

    public static int getGenderFromId(int nItemID) {
        int result;

        if (nItemID / 1000000 != 1 && getItemPrefix(nItemID) != 254 || getItemPrefix(nItemID) == 119 || getItemPrefix(nItemID) == 168)
            return 2;
        switch (nItemID / 1000 % 10) {
            case 0:
                result = 0;
                break;
            case 1:
                result = 1;
                break;
            default:
                return 2;
        }
        return result;
    }

    private static int getItemPrefix(int nItemID) {
        return nItemID / 10000;
    }
    public static boolean isWeapon(int itemID) {
        return itemID >= 1210000 && itemID < 1600000 || itemID / 10000 == 170;
    }
}

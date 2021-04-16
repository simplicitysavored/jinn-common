package xyz.yuanjin.project.common.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 表格单元格列
 *
 * @author yuanjin
 */
@SuppressWarnings("unused")
public enum ExcelCellEnum {
    /**
     * 表格单元格列
     */
    A(0),
    B(1),
    C(2),
    D(3),
    E(4),
    F(5),
    G(6),
    H(7),
    I(8),
    J(9),
    K(10),
    L(11),
    M(12),
    N(13),
    O(14),
    P(15),
    Q(16),
    R(17),
    S(18),
    T(19),
    U(20),
    V(21),
    W(22),
    X(23),
    Y(24),
    Z(25),
    AA(26),
    AB(27),
    AC(28),
    AD(29),
    AE(30),
    AF(31),
    AG(32),
    AH(33),
    AI(34),
    AJ(35),
    AK(36),
    AL(37),
    AM(38),
    AN(39),
    AO(40),
    AP(41),
    AQ(42),
    AR(43),
    AS(44),
    AT(45),
    AU(46),
    AV(47),
    AW(48),
    AX(49),
    AY(50),
    AZ(51),
    BA(52),
    BB(53),
    BC(54),
    BD(55),
    BE(56),
    BF(57),
    BG(58),
    BH(59),
    BI(60),
    BJ(61),
    BK(62),
    BL(63),
    BM(64),
    BN(65),
    BO(66),
    BP(67),
    BQ(68),
    BR(69),
    BS(70),
    BT(71),
    BU(72),
    BV(73),
    BW(74),
    BX(75),
    BY(76),
    BZ(77),
    CA(78),
    CB(79),
    CC(80),
    CD(81),
    CE(82),
    CF(83),
    CG(84),
    CH(85),
    CI(86),
    CJ(87),
    CK(88),
    CL(89),
    CM(90),
    CN(91),
    CO(92),
    CP(93),
    CQ(94),
    CR(95),
    CS(96),
    CT(97),
    CU(98),
    CV(99),
    CW(100),
    CX(101),
    CY(102),
    CZ(103),
    DA(104),
    DB(105),
    DC(106),
    DD(107),
    DE(108),
    DF(109),
    DG(110),
    DH(111),
    DI(112),
    DJ(113),
    DK(114),
    DL(115),
    DM(116),
    DN(117),
    DO(118),
    DP(119),
    DQ(120),
    DR(121),
    DS(122),
    DT(123),
    DU(124),
    DV(125),
    DW(126),
    DX(127),
    DY(128),
    DZ(129),
    ;

    private int value;

    ExcelCellEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int getValue(String colTag) {
        ExcelCellEnum excelCellEnum = ExcelCellEnum.valueOf(ExcelCellEnum.class, colTag);
        return excelCellEnum.getValue();
    }

    public static ExcelCellEnum getColTag(int colValue) {
        return Arrays.stream(ExcelCellEnum.values()).filter(item -> item.getValue() == colValue).collect(Collectors.toList()).get(0);
    }

    public static void main(String[] args) {
        System.out.println(ExcelCellEnum.getValue("CZ"));
    }

}

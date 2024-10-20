library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity shift_reg8_tb is
end shift_reg8_tb;

architecture Behavioral of shift_reg8_tb is

component shift_reg8 is
    Port ( clk : in STD_LOGIC;
           en : in STD_LOGIC;
           si : in STD_LOGIC;
           so : out STD_LOGIC);
end component;

signal clk, en, si, so : std_logic := '0';
constant T : time := 10ns;

begin
    clock_generator : process
    begin
        clk <= '0';
        wait for T / 2;
        clk <= '1';
        wait for T / 2;
    end process;

    process
    begin
        en <= '1';
        wait for T;
        si <= '1';
        wait for T;
        si <= '1';
        wait for T;
        si <= '0';
        wait for T;
        en <= '0';
        wait for T;
        si <= '0';
        wait for T;
        si <= '1';
        wait for T;
        si <= '0';
        wait for T;
        si <= '1';
        wait for T;
        en <= '1';
        wait for T;
        si <= '0';
        wait for T;
        si <= '1';
        wait for T;
        en <= '1';
        wait for T;
        si <= '1';
    end process;

    mapare : shift_reg8 port map(
        clk => clk,
        en => en,
        si => si,
        so => so
    );

end Behavioral;

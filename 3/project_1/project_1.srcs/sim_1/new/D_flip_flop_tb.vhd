library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity D_flip_flop_tb is
end D_flip_flop_tb;

architecture Behavioral of D_flip_flop_tb is

component D_flip_flop is
    Port ( clk : in STD_LOGIC;
           d : in STD_LOGIC;
           q : out STD_LOGIC);
end component;

signal clk, d, q : std_logic := '0';
constant CLOCK : time := 10ns;

begin

    clock_generator : process
    begin
        clk <= '0';
        wait for CLOCK / 2;
        clk <= '1';
        wait for CLOCK / 2;
    end process;
    
    process
    begin
        wait for CLOCK;
        d <= '0';
        wait for CLOCK;
        d <= '1';
        wait for CLOCK;
    end process;

    mapare : D_flip_flop port map(
        clk => clk,
        d => d,
        q => q
    );
end Behavioral;

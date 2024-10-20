library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity reg8_tb is
end reg8_tb;

architecture testbench of reg8_tb is

component reg8 is
    Port ( clk : in STD_LOGIC;
           en : in STD_LOGIC;
           d : in STD_LOGIC_VECTOR (7 downto 0);
           q : out STD_LOGIC_VECTOR (7 downto 0));
end component;

signal clk, en : std_logic := '0';
signal d, q : std_logic_vector(7 downto 0) := "00000000";
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
        d <= "10100110";
        wait for CLOCK;
        en <= '1';
        wait for CLOCK;
        d <= "00110011";
        en <= '0';
        wait for CLOCK;
        d <= "11111111";
        wait for CLOCK;
        en <= '1';
        wait for CLOCK;
        en <= '0';
        wait for CLOCK;
        d <= "00000000";
        wait for CLOCK;
        en <= '1';
    end process;   

mapare : reg8 port map(
    clk => clk,
    d => d,
    en => en,
    q => q
);

end testbench;

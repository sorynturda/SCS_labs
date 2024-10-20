library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity bit8_mux_4_1_tb is
end bit8_mux_4_1_tb;

architecture tb of bit8_mux_4_1_tb is

component bit8_mux_4_1 is
    Port ( a : out STD_LOGIC_VECTOR (7 downto 0);
        b : out STD_LOGIC_VECTOR (7 downto 0);
        c : out STD_LOGIC_VECTOR (7 downto 0);
        d : out STD_LOGIC_VECTOR (7 downto 0);
        sel : in STD_LOGIC_VECTOR (1 downto 0);
        x : in STD_LOGIC_VECTOR (7 downto 0));
end component;

signal a,b,c,d,x : STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
signal sel : std_logic_vector (1 downto 0) := (others => '0');
constant T : time := 10ns;
begin

    stimuli : process
    begin
        x <= "10101010";
        wait for T;
        sel <= "01";
        wait for T;
        sel <= "10";
        wait for T;
        sel <= "11";
    end process stimuli;
    
    mapare : bit8_mux_4_1 port map(
        a => a,
        b => b,
        c => c,
        d => d,
        x => x,
        sel => sel
    );
end tb;

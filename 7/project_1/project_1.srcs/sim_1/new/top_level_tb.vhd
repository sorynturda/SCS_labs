library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity top_level_tb is
end top_level_tb;

architecture tb of top_level_tb is

component top_level is
    Port ( aclk : in std_logic;
           a : in STD_LOGIC_VECTOR (31 downto 0);
           min : in STD_LOGIC_VECTOR (31 downto 0);
           max : in STD_LOGIC_VECTOR (31 downto 0);
           sum : out STD_LOGIC_VECTOR (31 downto 0));
end component;


constant T : time := 10ns;
signal aclk : std_logic := '0';
signal a,min,max,sum : std_logic_vector (31 downto 0) := (others => '0');


begin

    clock_generator : process
    begin
        aclk <= '0';
        wait for T / 2;
        aclk <= '1';
        wait for T / 2;
    end process clock_generator;  
    
    stimuli : process
    begin
        min <= x"00000001";
        max <= x"00000009";
        a <= x"00000001";
        wait for T;
        a <= x"00000002";
        wait for T;
        a <= x"00000003";
        wait for T;
    end process stimuli;
    
    t_l_tb : top_level port map(
        a => a,
        min => min,
        max => max,
        sum => sum,
        aclk => aclk
    );

end tb;

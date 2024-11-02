library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity fifo_8bit_tb is
end fifo_8bit_tb;

architecture tb of fifo_8bit_tb is

component fifo_8bit is
    Port ( rd : in STD_LOGIC;
           data_in : in STD_LOGIC_VECTOR (7 downto 0);
           wr : in STD_LOGIC;
           wr_inc : in STD_LOGIC;
           rd_inc : in STD_LOGIC;
           rst : in STD_LOGIC;
           clk : in STD_LOGIC;
           data_out : out STD_LOGIC_VECTOR (7 downto 0));
end component;

signal data_in, data_out : std_logic_vector (7 downto 0) := (others => '0');
signal rd, wr, wr_inc, rd_inc, rst, clk : std_logic := '0';
constant T : time := 10ns;

begin
    clk_generator : process
    begin
        clk <= '1';
        wait for T/2;
        clk <= '0';
        wait for T/2;
    end process clk_generator;
    
    stimuli : process
    begin
        data_in <= "11110000";
        wait for T;
        wr <= '1';
        wait for T;
        wr <= '0';
        wait for T;
        rd <= '1';
        wait for T;
        wr_inc <= '1';
        wait for T;
        wr_inc <= '0';
        data_in <= "10101010";
        wait for T;
        wr <= '1';
        wait for T;
        wr <= '0';
        wait for T;
        wr_inc <= '1';
        wait for T;
        wr_inc <= '0';
        wait for T;
        data_in <= "10100000";
        wait for T;
        wr <= '1';
        wait for T;
        wr <= '0';
        wait for T;
        rd_inc <= '1';
        wait for T;
        rd_inc <= '0';
        wait for T;
        rd_inc <= '1';
        wait for T;
        rd_inc <= '0';
        wait for T;
        rd_inc <= '1';
        wait for T;
        rd_inc <= '0';
        wait for T;
        rst <= '1';
        wait for T;
        rst <= '0';
    end process stimuli;
    
    mapare : fifo_8bit port map(
        rd => rd,
        data_in => data_in,
        wr => wr,
        wr_inc => wr_inc, 
        rd_inc => rd_inc,
        rst => rst,
        clk => clk,
        data_out => data_out
    );

end tb;

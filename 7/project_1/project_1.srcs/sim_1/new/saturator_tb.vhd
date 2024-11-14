library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity saturator_tb is
end saturator_tb;

architecture tb of saturator_tb is

component saturator is
    Port (
        aclk : IN STD_LOGIC;
        s_axis_val_tvalid : IN STD_LOGIC;
        s_axis_val_tready : OUT STD_LOGIC;
        s_axis_val_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        s_axis_max_tvalid : IN STD_LOGIC;
        s_axis_max_tready : OUT STD_LOGIC;
        s_axis_max_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        s_axis_min_tvalid : IN STD_LOGIC;
        s_axis_min_tready : OUT STD_LOGIC;
        s_axis_min_tdata : IN STD_LOGIC_VECTOR(31 DOWNTO 0);
        m_axis_result_tvalid : OUT STD_LOGIC;
        m_axis_result_tready : IN STD_LOGIC;
        m_axis_result_tdata : OUT STD_LOGIC_VECTOR(31 DOWNTO 0)
        );
end component;

constant T : time := 10ns;
signal aclk, s_axis_val_tvalid, s_axis_max_tvalid, s_axis_val_tready, s_axis_max_tready : std_logic := '0';
signal s_axis_min_tvalid, s_axis_min_tready, m_axis_result_tvalid, m_axis_result_tready : std_logic := '0';
signal m_axis_result_tdata, s_axis_min_tdata, s_axis_max_tdata, s_axis_val_tdata : std_logic_vector(31 downto 0) := (others => '0');

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
        s_axis_min_tdata <= x"00000005";
        s_axis_max_tdata <= x"0000000A";
        s_axis_val_tdata <= x"00000001";
        wait for T;
        s_axis_min_tvalid <= '1';
        s_axis_max_tvalid <= '1';
        s_axis_val_tvalid <= '1';
        wait for T;
        m_axis_result_tready <= '1';   
        wait for T;
    end process stimuli;
    
    sat : saturator port map(aclk, s_axis_val_tvalid, s_axis_val_tready, s_axis_val_tdata, s_axis_max_tvalid,
         s_axis_max_tready, s_axis_max_tdata, s_axis_min_tvalid, s_axis_min_tready, s_axis_min_tdata,
         m_axis_result_tvalid, m_axis_result_tready, m_axis_result_tdata);
end tb;

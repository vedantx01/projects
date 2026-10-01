# Zombie and orphan processes

A **zombie** has finished execution but retains a minimal process-table entry
until its parent reads its exit status. An **orphan** is still running after its
parent exits. Real operating systems typically re-parent an orphan to a system
process so it can eventually be collected. The lab marks orphans explicitly
and retains the original parent ID for teaching visibility.
